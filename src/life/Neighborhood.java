package life;

import life.patterns.Pattern;
import util.lambdas.ThreeCoords;
import windows.Life;

public class Neighborhood {
    protected final double radius = 1.5;

    protected Cell[][][] cells;

    public Neighborhood(Pattern p) {
        int[] d = p.dim();
        this.cells = new Cell[d[0]][d[1]][d[2]];
        for (int x = 0; x < d[0]; x++) {
            for (int y = 0; y < d[1]; y++) {
                for (int z = 0; z < d[2]; z++) {
                    double state = p.getState(x, y, z);
                    this.cells[x][y][z] = new Cell(x, y, z, state);
                }
            }
        }
    }

    public void update() {
        this.forEach((int x, int y, int z) -> {
            for (int oz = (int) -radius; oz <= radius; oz++) {
                double xMinMax = Math.sqrt(Math.pow(radius, 2) - Math.pow(oz, 2));
                for (int ox = (int) -xMinMax; ox <= xMinMax; ox++) {
                    double yMinMax = Math.sqrt(Math.pow(radius, 2) - Math.pow(ox, 2));
                    for (int oy = (int) -yMinMax; oy <= yMinMax; oy++) {
                        int tx = (int) Math.abs((x + ox) % this.cells.length);
                        int ty = (int) Math.abs((y + oy) % this.cells[tx].length);
                        int tz = (int) Math.abs((z + oz) % this.cells[tx][ty].length);

                        Cell c = this.cells[x][y][z];
                        Cell c1 = this.cells[tx][ty][tz];
                        c.interact(c1);
                    }
                }
            }
        });
    }

    public void display() {
        this.forEach((x, y, z) -> {
            Life.getInstance().pushMatrix();
            Life.getInstance().translate(-this.cells.length / 2 * Cell.SIZE, -this.cells[0].length / 2 * Cell.SIZE,
                    -this.cells[0][0].length / 2 * Cell.SIZE);
            this.cells[x][y][z].display();
            Life.getInstance().popMatrix();
        });
    }

    public Cell getCell(int x, int y, int z) {
        return this.cells[x][y][z];
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
