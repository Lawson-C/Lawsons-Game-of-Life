package game.worlds;

import util.*;

import game.worlds.generators.Generator;
import processing.core.PApplet;

public class World {
    protected PApplet game;
    protected int blockSize;
    protected Grid<Chunk> data;

    public World(PApplet game, Generator g, int blockSize) {
        this.game = game;
        this.blockSize = blockSize;
        this.data = new Grid<Chunk>(1, 1, 1, 1);
        int[] dim = this.data.size(); // dimensions of data grid
        for (int x = -dim[0]; x < dim[1]; x++) {
            for (int y = -dim[2]; y < dim[3]; y++) {
                this.data.set(x, y, new Chunk(game, x, y, blockSize, g));
            }
        }
    }

    public void display() {
        int[] dim = this.data.size(); // dimensions of data grid
        for (int x = -dim[0]; x < dim[1]; x++) {
            for (int y = -dim[2]; y < dim[3]; y++) {
                this.data.get(x, y).display();
            }
        }
    }
}
