package game.worlds;

import processing.core.PVector;
import util.grids.LoopGrid;
import windows.GameApp;

public class World {
    final protected double renderDist = 2; // measured in units of chunks

    protected volatile LoopGrid<Chunk> data;

    protected GameApp game;
    protected PVector spawnPoint;

    public World(GameApp game) {
        this.game = game;
        this.data = new LoopGrid<Chunk>(2, 2, 2, 2);
        int[] dim = this.data.size(); // dimensions of data grid
        for (int x = -dim[0]; x < dim[1]; x++) {
            for (int y = -dim[2]; y < dim[3]; y++) {
                this.data.set(x, y, new Chunk(this, x, y, game.getGen()));
            }
        }
    }

    public void renderBlocks() {
        PVector center = this.game.getP1().getPos();
        center.div(Chunk.blockSize);
        for (int x = (int) (center.x - this.renderDist * Chunk.width); x < center.x
                + this.renderDist * Chunk.width; x++) {
            for (int y = 0; y < Chunk.height; y++) {
                double minMax = Math.sqrt(Math.pow(this.renderDist * Chunk.width, 2) - Math.pow(x - center.x, 2));
                for (int z = (int) (center.z - minMax); z < center.z + minMax; z++) {
                    this.game.pushMatrix();
                    this.game.translate(x * Chunk.blockSize, y * Chunk.blockSize + Chunk.rawHeight(), z * Chunk.blockSize);
                    this.getBlock(x, y, z).display();
                    this.game.popMatrix();
                }
            }
        }
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
        p.x /= Chunk.rawWidth();
        p.z /= Chunk.rawWidth();
        if (p.x < 0) {
            p.x -= 1;
        }
        if (p.z < 0) {
            p.z -= 1;
        }
        return this.data.get((int) (p.x), (int) (p.z));
    }

    public Chunk getChunkRaw(PVector p) {
        return this.getChunkRaw(p.x, p.z);
    }

    /*
     * returns block based on world index (looped)
     */
    public Block getBlock(int x, int y, int z) {
        int l = this.getSize()[0] * Chunk.width;
        x = Math.abs((x + (int) (x / l + l) * l) % l);
        l = this.getSize()[1] * Chunk.width;
        z = Math.abs((z + (int) (z / l + l) * l) % l);
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

    public int[] getSize() {
        return this.data.size(false);
    }
}
