package life;

import life.patterns.Pattern;
import util.lambdas.ThreeCoords;
import windows.Life;

public class Neighborhood {
    protected final double radius = 1.5;

    protected Life window;
    protected Cell[][][] cells;

    public Neighborhood(Life window, Pattern p, int lx, int ly, int lz) {
        this.window = window;
        this.cells = new Cell[lx][ly][lz];
        for (int x = 0; x < lx; x++) {
            for (int y = 0; y < ly; y++) {
                for (int z = 0; z < lz; z++) {
                    float state = p.getState(x, y, z);
                    this.cells[x][y][z] = new Cell(window, x, y, z, state);
                }
            }
        }
    }

    public void update() {
        this.forEach((int x, int y, int z) -> {
            for (double oz = -radius; oz < radius; oz++) {
                double xMinMax = Math.sqrt(Math.pow(radius, 2) - Math.pow(oz, 2));
                for (double ox = -xMinMax; ox < xMinMax; ox++) {
                    double yMinMax = Math.sqrt(Math.pow(radius, 2) - Math.pow(ox, 2));
                    for (double oy = -yMinMax; oy < yMinMax; oy++) {
                        int tx = (int) Math.abs((x + ox) % this.cells.length);
                        int ty = (int) Math.abs((y + oy) % this.cells[tx].length);
                        int tz = (int) Math.abs((z + oz) % this.cells[tx][ty].length);

                        Cell c = this.cells[x][y][z];
                        Cell c1 = this.cells[tx][ty][tz];
                        double diff = c.getState() - c1.getState();
                        double mag = 0.5;
                        c.addState(-diff * mag);
                        c1.addState(diff * mag);
                    }
                }
            }
        });
    }

    public void display() {
        this.forEach((x, y, z) -> {
            this.window.pushMatrix();
            this.window.translate(-this.cells.length / 2 * Cell.SIZE, -this.cells[0].length / 2 * Cell.SIZE,
                    -this.cells[0][0].length / 2 * Cell.SIZE);
            this.cells[x][y][z].display();
            this.window.popMatrix();
        });
    }

    public void forEach(ThreeCoords cb) {
        for (int x = 0; x < this.cells.length / 2; x++) {
            for (int y = 0; y < this.cells[x].length / 2; y++) {
                for (int z = 0; z < this.cells[x][y].length; z++) {
                    cb.run(x, y, z);
                }
            }
        }
        for (int x = this.cells.length / 2; x < this.cells.length; x++) {
            for (int y = 0; y < this.cells[x].length / 2; y++) {
                for (int z = 0; z < this.cells[x][y].length; z++) {
                    cb.run(x, y, z);
                }
            }
        }
        for (int x = 0; x < this.cells.length / 2; x++) {
            for (int y = this.cells[x].length / 2; y < this.cells[x].length; y++) {
                for (int z = 0; z < this.cells[x][y].length; z++) {
                    cb.run(x, y, z);
                }
            }
        }
        for (int x = this.cells.length / 2; x < this.cells.length; x++) {
            for (int y = this.cells[x].length / 2; y < this.cells[x].length; y++) {
                for (int z = 0; z < this.cells[x][y].length; z++) {
                    cb.run(x, y, z);
                }
            }
        }
    }
}
