package game.worlds;

import game.worlds.generators.Generator;
import processing.core.PApplet;

public class Chunk {
    public static final int width = 8, height = 16;

    private PApplet game;
    private World world;
    private int indx, indy;
    private int blockSize;
    private Block[][][] data;

    public Chunk(PApplet game, World world, int indx, int indy, Generator g) {
        this.game = game;
        this.world = world;
        this.indx = indx;
        this.indy = indy;
        this.blockSize = world.getSize();
        this.data = g.makeChunk(this, this.world, indx, indy);
    }

    public void display() {
        this.game.pushMatrix();
        this.game.translate(this.indx * this.rawWidth(), this.game.height, this.indy * this.rawWidth());
        chunkStroke();
        for (int x = 0; x < Chunk.width; x++) {
            for (int y = 0; y < Chunk.height; y++) {
                for (int z = 0; z < Chunk.width; z++) {
                    this.data[x][y][z].blockMatrix();
                }
            }
        }
        this.game.popMatrix();
    }

    public void chunkStroke() {
        this.game.pushMatrix();
        this.game.noFill();
        this.game.stroke(255, 0, 0);
        this.game.strokeWeight(5);
        this.game.translate(this.rawWidth() / 2, this.rawHeight() / 2, this.rawWidth() / 2);
        this.game.box(this.rawWidth() + 1, this.rawHeight() + 1, this.rawWidth() + 1);
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
