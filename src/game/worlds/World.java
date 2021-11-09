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
        Chunk centerChunk = this.getChunkRaw(this.game.getP1().getPos());
        int[] low = new int[] { centerChunk.getIndx() - this.renderDist, centerChunk.getIndz() - this.renderDist };
        for (int x = low[0]; x <= low[0] + 2 * this.renderDist; x++) {
            for (int z = low[1]; z <= low[1] + 2 * this.renderDist; z++) {
                this.data.get(x, z).display();
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
