package game.worlds;

import game.worlds.generators.Generator;
import processing.core.PVector;
import windows.GameApp;

public class Chunk {
    public static final int width = 8, height = 16;

    private GameApp game;
    private World world;
    private int indx, indz;
    private Block[][][] data;

    public Chunk(World world, int indx, int indz, Generator g) {
        this.game = world.game;
        this.world = world;
        this.indx = indx;
        this.indz = indz;
        this.data = g.makeChunk(this, this.world, indx, indz);
    }

    /*
     * world must call translate() beforehand
     */
    public void display() {
        for (int x = 0; x < Chunk.width; x++) {
            for (int y = 0; y < Chunk.height; y++) {
                for (int z = 0; z < Chunk.width; z++) {
                    this.data[x][y][z].blockMatrix();
                }
            }
        }
    }

    public void chunkStroke() {
        this.game.pushMatrix();
        this.game.noFill();
        this.game.stroke(255, 0, 0);
        this.game.strokeWeight(5);
        PVector coords = this.getRawCoords();
        this.game.translate(coords.x + Chunk.rawWidth() / 2, coords.y + Chunk.rawHeight() / 2,
                coords.z + Chunk.rawWidth() / 2);
        this.game.box(Chunk.rawWidth(), Chunk.rawHeight(), Chunk.rawWidth());
        this.game.popMatrix();
    }

    public void updateStates(float[][][] s) {
        for (int x = 0; x < Chunk.width; x++) {
            for (int y = 0; y < Chunk.height; y++) {
                for (int z = 0; z < Chunk.width; z++) {
                    this.data[x][y][z].updateState(s[x][y][z]);
                }
            }
        }
    }

    public void setBlock(int x, int y, int z, Block b) {
        this.data[x][y][z] = b;
    }

    public Block getBlock(int x, int y, int z) {
        return this.data[x][y][z];
    }

    public GameApp getApplet() {
        return this.game;
    }

    public World getWorld() {
        return this.world;
    }

    public int getIndx() {
        return indx;
    }

    public int getIndz() {
        return indz;
    }

    public PVector getRawCoords() {
        return new PVector(this.indx * Chunk.rawWidth(), Chunk.rawHeight(), this.indz * Chunk.rawWidth());
    }

    /*
     * returns block based on raw coords
     */
    public Block getBlockRaw(float x, float y, float z) {
        PVector corner = this.getRawCoords();
        double xi = (x - corner.x) / ((double) Block.size);
        double yi = (y - corner.y) / ((double) Block.size);
        double zi = (z - corner.z) / ((double) Block.size);
        xi = (int) Math.abs(xi) * Math.signum(xi);
        yi = (int) Math.abs(yi) * Math.signum(yi);
        zi = (int) Math.abs(zi) * Math.signum(zi);
        return this.data[(int) (xi)][(int) (yi)][(int) (zi)];
    }

    public static int rawWidth() {
        return Chunk.width * Block.size;
    }

    public static int rawHeight() {
        return Chunk.height * Block.size;
    }
}
