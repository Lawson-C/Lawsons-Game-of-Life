package game.worlds;

import game.worlds.generators.Generator;
import processing.core.PVector;
import util.LoopGrid;
import windows.GameApp;

public class World {
    protected int renderDist = 1;
    protected GameApp game;
    protected int blockSize;
    protected PVector spawnPoint;
    protected LoopGrid<Chunk> data;

    public World(GameApp game, Generator g, int blockSize) {
        this.game = game;
        this.blockSize = blockSize;
        this.data = new LoopGrid<Chunk>(10, 10, 10, 10);
        int[] dim = this.data.size(); // dimensions of data grid
        for (int x = -dim[0]; x < dim[1]; x++) {
            for (int y = -dim[2]; y < dim[3]; y++) {
                this.data.set(x, y, new Chunk(this, x, y, g));
            }
        }
    }

    public int getSize() {
        return this.blockSize;
    }

    public void display(boolean loop) {
        PVector center = new PVector().set(this.game.getP1().getPos());
        center.x /= Chunk.rawWidth();
        center.z /= Chunk.rawWidth();
        for (int x = (int) center.x - this.renderDist - 1; x <= center.x + this.renderDist; x++) {
            for (int z = (int) center.z - this.renderDist - 1; z <= center.z + this.renderDist; z++) {
                this.game.pushMatrix();
                this.game.translate(x * Chunk.rawWidth(), this.data.get(x, z).getRawCoords().y, z * Chunk.rawWidth());
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
     * returns chunk based on raw coordinates
     */
    public Chunk getChunkRaw(float x, float y, float z) {
        int[] bIndex = new int[] { (int) (x / this.blockSize), (int) (z / this.blockSize) }; // only uses x and z
                                                                                             // because chunks have the
                                                                                             // same y
        int[] cIndex = new int[] { (int) (bIndex[0] / Chunk.width), (int) (bIndex[1] / Chunk.width) };
        return this.data.get(cIndex[0], cIndex[1]);
    }

    public Chunk getChunkRaw(PVector p) {
        return this.getChunkRaw(p.x, p.y, p.z);
    }
}
