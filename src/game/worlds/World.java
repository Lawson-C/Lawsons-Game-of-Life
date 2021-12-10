package game.worlds;

import game.character.Player;
import processing.core.PVector;
import util.grids.LoopGrid;
import util.lambdas.ThreeCoords;
import windows.GameApp;

public class World {
    final protected double renderDist = 2; // measured in units of chunks

    protected volatile LoopGrid<Chunk> data;

    protected GameApp game;
    protected PVector spawnPoint;

    public World(GameApp game) {
        this.game = game;
        this.data = new LoopGrid<Chunk>(10, 10, 10, 10);
        int[] dim = this.data.size(); // dimensions of data grid
        for (int x = -dim[0]; x < dim[1]; x++) {
            for (int y = -dim[2]; y < dim[3]; y++) {
                this.data.set(x, y, new Chunk(this, x, y, game.getGen()));
            }
        }
    }

    public void renderBlocks() {
        Block target = this.game.getP1().targetBlock();
        this.forRenderBlocks((x, y, z) -> {
            this.game.pushMatrix();
            Block b = this.getBlock(x, y, z);
            this.game.translate((x + .5f) * Block.size, (y + .5f) * Block.size + Chunk.rawHeight(),
                    (z + .5f) * Block.size);
            b.display();
            if (b == target) {
                this.game.noFill();
                this.game.strokeWeight(5);
                this.game.stroke(255, 255, 0);
                this.game.box(Block.size);
            }
            this.game.popMatrix();
        });
    }

    public void renderChunks() {
        PVector center = this.game.getP1().getPos();
        center.x /= Chunk.rawWidth();
        center.z /= Chunk.rawWidth();
        for (int x = (int) (center.x - this.renderDist - 1); x <= center.x + this.renderDist; x++) {
            double minMax = Math.sqrt(Math.pow(this.renderDist, 2) - Math.pow(x, 2));
            for (int z = (int) (center.z - minMax); z <= center.z + minMax; z++) {
                this.game.pushMatrix();
                this.game.translate(x * Chunk.rawWidth(), Chunk.rawHeight(), z * Chunk.rawWidth());
                this.data.get(x, z).display();
                this.game.popMatrix();
            }
        }
    }

    public int[] getSize() {
        return this.data.size(false);
    }

    public PVector getSpawn() {
        return this.spawnPoint;
    }

    public void setSpawn(float x, float y, float z) {
        this.spawnPoint = new PVector(x, y, z);
    }

    /*
     * returns chunk corresponding on raw coordinates
     */
    public Chunk getChunkRaw(float x, float z) {
        PVector p = this.getWorldCoords(x, z);
        x = p.x / Chunk.rawWidth();
        z = p.z / Chunk.rawWidth();
        if (x < 0) {
            x -= 1;
        }
        if (z < 0) {
            z -= 1;
        }
        return this.data.get((int) (x), (int) (z));
    }

    public Chunk getChunkRaw(PVector p) {
        return this.getChunkRaw(p.x, p.z);
    }

    /*
     * returns block based on world index (looped)
     */
    public Block getBlock(int x, int y, int z) {
        int l = this.getSize()[0] * Chunk.width;
        x = LoopGrid.loopIndex(x, l);
        l = this.getSize()[1] * Chunk.width;
        z = LoopGrid.loopIndex(z, l);
        return this.data.get(x / Chunk.width, z / Chunk.width).getBlock(x % Chunk.width, y, z % Chunk.width);
    }

    /*
     * returns block corresponding to raw coordinates
     */
    public Block getBlockRaw(float x, float y, float z) {
        PVector p = this.getWorldCoords(x, y, z);
        return this.getChunkRaw(p.x, p.z).getBlockRaw(p.x, p.y, p.z);
    }

    public Block getBlockRaw(PVector p) {
        return this.getBlockRaw(p.x, p.y, p.z);
    }

    /*
     * returns coordinates with consideration for the looped world
     */
    public PVector getWorldCoords(float x, float y, float z) {
        int[] dim = this.data.size();
        if (x < 0) {
            x %= dim[0] * Chunk.rawWidth() * 1.f;
        } else {
            x %= dim[1] * Chunk.rawWidth() * 1.f;
        }
        if (z < 0) {
            z %= dim[2] * Chunk.rawWidth() * 1.f;
        } else {
            z %= dim[3] * Chunk.rawWidth() * 1.f;
        }
        return new PVector(x, y, z);
    }

    // ignore y
    public PVector getWorldCoords(float x, float z) {
        return this.getWorldCoords(x, 0, z);
    }

    /*
     * Iterates through every block in fov and render distance then runs callback
     * function
     */
    public void forRenderBlocks(ThreeCoords cb) {
        Player p1 = this.game.getP1();
        double rad = this.renderDist * Chunk.width;
        double halfFov = p1.getFOV() / 2. + Math.PI / 8;
        double pangle = p1.lookAngle().x - Math.PI / 2;
        PVector playerPos = p1.getPos().div(Block.size);
        playerPos.add((float) (-4 * Math.cos(pangle)), 0, (float) (-4 * Math.sin(pangle)));
        for (double x = -rad; x < rad; x++) {
            double zBound = Math.sqrt(Math.pow(rad, 2) - Math.pow(x, 2));
            for (double z = -zBound; z < zBound; z++) {
                double low = pangle - halfFov;
                double high = pangle + halfFov;
                double angle = Math.atan2(z, x);

                if (angle < 0 && pangle > Math.PI - halfFov) {
                    angle += 2 * Math.PI;
                }
                if (angle > 0 && pangle < -Math.PI + halfFov) {
                    angle -= 2 * Math.PI;
                }
                if (angle < low) {
                    continue;
                }
                if (angle > high) {
                    continue;
                }

                for (int y = 0; y < Chunk.height; y++) {
                    cb.run((int) (x + playerPos.x), y, (int) (z + playerPos.z));
                }
            }
        }
    }

    public void placeBlockRaw(float x, float y, float z, String block) {
        Block b = this.getBlockRaw(x, y, z);
        this.getChunkRaw(x, z).setBlockRaw(x, y, z, b.changeType(block));
    }
}
