package game.worlds;

import game.worlds.generators.Generator;
import processing.core.PApplet;
import processing.core.PVector;

public class Chunk {
    public static final int width = 8, height = 16;

    private PApplet game;
    private World world;
    private int indx, indz;
    private int blockSize;
    private Block[][][] data;

    public Chunk(PApplet game, World world, int indx, int indz, Generator g) {
        this.game = game;
        this.world = world;
        this.indx = indx;
        this.indz = indz;
        this.blockSize = world.getSize();
        this.data = g.makeChunk(this, this.world, indx, indz);
    }

    public void display() {
        this.game.pushMatrix();
        PVector coords = this.getRawCoords();
        this.game.translate(coords.x, coords.y, coords.z);
        for (int x = 0; x < Chunk.width; x++) {
            for (int y = 0; y < Chunk.height; y++) {
                for (int z = 0; z < Chunk.width; z++) {
                    this.data[x][y][z].blockMatrix();
                }
            }
        }
        this.game.popMatrix();
        chunkStroke();
    }

    public void chunkStroke() {
        this.game.pushMatrix();
        this.game.noFill();
        this.game.stroke(255, 0, 0);
        this.game.strokeWeight(5);
        PVector coords = this.getRawCoords();
        this.game.translate(coords.x + this.rawWidth() / 2, coords.y + this.rawHeight() / 2,
                coords.z + this.rawWidth() / 2);
        this.game.box(this.rawWidth(), this.rawHeight(), this.rawWidth());
        this.game.popMatrix();
    }

    public void setBlock(int x, int y, int z, Block b) {
        this.data[x][y][z] = b;
    }

    public PApplet getApplet() {
        return this.game;
    }

    public int getIndx() {
        return indx;
    }

    public int getIndz() {
        return indz;
    }

    public int rawWidth() {
        return Chunk.width * this.blockSize;
    }

    public int rawHeight() {
        return Chunk.height * this.blockSize;
    }

    public PVector getRawCoords() {
        return new PVector(this.indx * this.rawWidth(), this.game.height - this.rawHeight(),
                this.indz * this.rawWidth());
    }
}
