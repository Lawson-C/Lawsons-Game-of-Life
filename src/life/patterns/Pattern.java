package life.patterns;

import util.lambdas.ThreeCoords;

public abstract class Pattern {
    protected double[][][] states;

    public Pattern(int lx, int ly, int lz) {
        this.states = new double[lx][ly][lz];

    }

    public void makeStateMap() {
        for (int x = 0; x < this.states.length; x++) {
            for (int y = 0; y < this.states[x].length; y++) {
                for (int z = 0; z < this.states[x][y].length; z++) {
                    this.states[x][y][z] = mapState(x, y, z);
                }
            }
        }
    }

    public void makeStateMap(ThreeCoords cb) {
        for (int x = 0; x < this.states.length; x++) {
            for (int y = 0; y < this.states[x].length; y++) {
                for (int z = 0; z < this.states[x][y].length; z++) {
                    cb.run(x, y, z);
                }
            }
        }
    }

    public int[] dim() {
        return new int[] { this.states.length, this.states[0].length, this.states[0][0].length };
    }

    public double getState(int x, int y, int z) {
        return this.states[x][y][z];
    }

    public abstract double mapState(int x, int y, int z);
}
