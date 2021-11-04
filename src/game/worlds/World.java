package game.worlds;

import util.*;

import game.worlds.generators.Generator;
import processing.core.PApplet;
import processing.core.PVector;

public class World {
    protected PApplet game;
    protected int blockSize;
    protected PVector spawnPoint;
    protected Grid<Chunk> data;

    public World(PApplet game, Generator g, int blockSize) {
        this.game = game;
        this.blockSize = blockSize;
        this.data = new Grid<Chunk>(1, 1, 1, 1);
        int[] dim = this.data.size(); // dimensions of data grid
        for (int x = -dim[0]; x < dim[1]; x++) {
            for (int y = -dim[2]; y < dim[3]; y++) {
                this.data.set(x, y, new Chunk(game, this, x, y, g));
            }
        }
    }

    public int getSize() {
        return this.blockSize;
    }

    public void display() {
        int[] dim = this.data.size(); // dimensions of data grid
        for (int x = -dim[0]; x < dim[1]; x++) {
            for (int y = -dim[2]; y < dim[3]; y++) {
                this.data.get(x, y).display();
            }
        }
    }

    public PVector getSpawn() {
        return this.spawnPoint;
    }

    public void setSpawn(float x, float y, float z) {
        this.spawnPoint = new PVector(x, y, z);
    }
}
