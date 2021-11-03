package game.worlds;

import game.worlds.generators.Generator;
import processing.core.PApplet;

public class Chunk {
    public static final int width = 16, height = 32;

    private PApplet game;
    private int indx, indy;
    private int blockSize;
    private Block[][][] data;

    public Chunk(PApplet game, int indx, int indy, int blockSize, Generator g) {
        this.indx = indx;
        this.indy = indy;
        this.blockSize = blockSize;
        this.data = g.makeChunk(this, indx, indy);
    }

    public void display() {
        this.game.pushMatrix();
        this.game.translate(this.indx * this.rawWidth(), this.indy * this.rawHeight());
        for (int x = 0; x < Chunk.width; x++) {
            for (int y = 0; y < Chunk.height; y++) {
                for (int z = 0; z < Chunk.width; z++) {
                    this.data[x][y][z].display();
                }
            }
        }
        this.game.popMatrix();
    }

    public void setBlock(int x, int y, int z, Block b) {
        this.data[x][y][z] = b;
    }

    public PApplet getApplet() {
        return this.game;
    }

    public int getIndy() {
        return indy;
    }

    public int getIndx() {
        return indx;
    }

    public int rawWidth() {
        return Chunk.width * this.blockSize;
    }

    public int rawHeight() {
        return Chunk.height * this.blockSize;
    }
}
