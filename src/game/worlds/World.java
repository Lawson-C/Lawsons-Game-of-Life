package game.worlds;

import game.character.Player;
import processing.core.PVector;
import util.grids.LoopGrid;
import util.lambdas.ThreeCoords;
import windows.GameApp;

public class World {
    protected final double renderDist = 3; // measured in units of chunks
    public static final int xLen = 4, zLen = 4; // must be even

    protected volatile LoopGrid<Chunk> data;

    protected PVector spawnPoint;

    public World() {
        this.data = new LoopGrid<Chunk>(xLen / 2, xLen / 2, zLen / 2, zLen / 2);
        for (int x = -xLen / 2; x < xLen / 2; x++) {
            for (int y = -zLen / 2; y < zLen / 2; y++) {
                this.data.set(x, y, new Chunk(this, x, y));
            }
        }
    }

    public void renderBlocks() {
        Block target = GameApp.getInstance().getP1().targetBlock();
        this.forRenderBlocks((x, y, z) -> {
            GameApp.getInstance().pushMatrix();
            Block b = this.getBlock(x, y, z);
            GameApp.getInstance().translate((x + .5f) * Block.size, (y + .5f) * Block.size + Chunk.rawHeight(),
                    (z + .5f) * Block.size);
            b.display();
            if (b == target) {
                GameApp.getInstance().noFill();
                GameApp.getInstance().strokeWeight(5);
                GameApp.getInstance().stroke(255, 255, 0);
                GameApp.getInstance().box(Block.size);
            }
            GameApp.getInstance().popMatrix();
        });
    }

    public void renderChunks() {
        PVector center = GameApp.getInstance().getP1().getPos();
        center.x /= Chunk.rawWidth();
        center.z /= Chunk.rawWidth();
        for (int x = (int) (center.x - this.renderDist - 1); x <= center.x + this.renderDist; x++) {
            double minMax = Math.sqrt(Math.pow(this.renderDist, 2) - Math.pow(x, 2));
            for (int z = (int) (center.z - minMax); z <= center.z + minMax; z++) {
                GameApp.getInstance().pushMatrix();
                GameApp.getInstance().translate(x * Chunk.rawWidth(), Chunk.rawHeight(), z * Chunk.rawWidth());
                this.data.get(x, z).display();
                GameApp.getInstance().popMatrix();
            }
        }
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
        x = (int) Math.floor(p.x / Chunk.rawWidth());
        z = (int) Math.floor(p.z / Chunk.rawWidth());
        return this.data.get((int) (x), (int) (z));
    }

    public Chunk getChunkRaw(PVector p) {
        return this.getChunkRaw(p.x, p.z);
    }

    /*
     * returns block based on world index (looped)
     */
    public Block getBlock(int x, int y, int z) {
        x = (int) LoopGrid.loopIndex(x, xLen * Chunk.width / 2, -xLen * Chunk.width / 2);
        z = (int) LoopGrid.loopIndex(z, zLen * Chunk.width / 2, -zLen * Chunk.width / 2);
        int ix = (int) Math.floor(x / (float) Chunk.width);
        int iz = (int) Math.floor(z / (float) Chunk.width);
        return this.data.get(ix, iz).getBlock((int) LoopGrid.loopIndex(x, Chunk.width), y,
                (int) LoopGrid.loopIndex(z, Chunk.width));
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
        x = (float) LoopGrid.loopIndex(x, rawWidth() / 2, -rawWidth() / 2);
        z = (float) LoopGrid.loopIndex(z, rawLength() / 2, -rawLength() / 2);
        return new PVector(x, y, z);
    }

    // ignore y
    public PVector getWorldCoords(float x, float z) {
        return this.getWorldCoords(x, 0, z);
    }

    public int rawWidth() {
        return xLen * Chunk.rawWidth();
    }

    public int rawHeight() {
        return Chunk.rawHeight();
    }

    public int rawLength() {
        return zLen * Chunk.rawWidth();
    }

    /*
     * Iterates through every block in fov and render distance then runs callback
     * function
     */
    public void forRenderBlocks(ThreeCoords cb) {
        Player p1 = GameApp.getInstance().getP1();
        double rad = this.renderDist * Chunk.width;
        double halfFov = p1.getFOV() / 2. + Math.PI / 8;
        double pangle = p1.lookAngle().x - Math.PI / 2;
        PVector playerPos = p1.getPos().div(Block.size);
        playerPos.add((float) (-4 * Math.cos(pangle)), 0, (float) (-4 * Math.sin(pangle)));
        for (double x = -rad; x <= rad; x++) {
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
                    cb.run((int) Math.floor(x + playerPos.x), y, (int) Math.floor(z + playerPos.z));
                }
            }
        }
    }

    public void placeBlockRaw(float x, float y, float z, String block) {
        Block b = this.getBlockRaw(x, y, z);
        this.getChunkRaw(x, z).setBlockRaw(x, y, z, b.changeType(block));
    }
}
