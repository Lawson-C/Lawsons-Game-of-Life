package life.patterns;

public abstract class Pattern {
    protected double[][][] states;

    public Pattern(int lx, int ly, int lz) {
        this.states = new double[lx][ly][lz];
    }

    public int[] dim() {
        return new int[] { this.states.length, this.states[0].length, this.states[0][0].length };
    }

    public abstract double getState(int x, int y, int z);
}
