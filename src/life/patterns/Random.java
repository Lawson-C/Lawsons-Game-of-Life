package life.patterns;

public class Random extends Pattern {
    public Random(int lx, int ly, int lz) {
        super(lx, ly, lz);
    }

    public double getState(int x, int y, int z) {
        double amount = Math.random();
        return amount;
    }
}