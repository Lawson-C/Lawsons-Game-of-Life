package life;

import life.patterns.Pattern;
import processing.core.PApplet;

public class Neighborhood {
    PApplet window;
    Cell[][][] cells;

    public Neighborhood(PApplet window, Pattern p, int lx, int ly, int lz) {
        this.window = window;
        this.cells = new Cell[lx][ly][lz];
        for (int x = 0; x < lx; x++) {
            for (int y = 0; y < ly; y++) {
                for (int z = 0; z < lz; z++) {
                    float state = p.getState(x, y, z, 0, 1);
                    this.cells[x][y][z] = new Cell(window, x, y, z, state);
                }
            }
        }
    }

    public void display() {
        for (Cell[][] c1 : cells) {
            for (Cell[] c2 : c1) {
                for (Cell c : c2) {
                    this.window.pushMatrix();
                    this.window.translate(-this.cells.length / 2 * Cell.SIZE, -this.cells[0].length / 2 * Cell.SIZE,
                            -this.cells[0][0].length / 2 * Cell.SIZE);
                    c.display();
                    this.window.popMatrix();
                }
            }
        }
    }
}
