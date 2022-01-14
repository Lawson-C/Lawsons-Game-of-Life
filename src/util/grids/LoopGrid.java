package util.grids;

public class LoopGrid<T> extends Grid<T> {
    public LoopGrid(int nx, int px, int ny, int py) {
        super(nx, px, ny, py);
    }

    public LoopGrid(int s) {
        super(s);
    }

    public LoopGrid(int x, int y) {
        super(x, y);
    }

    @Override
    public T get(int x, int y) {
        x = (int) loopIndex(x, super.px, -super.nx);
        y = (int) loopIndex(y, super.py, -super.ny);
        return super.get(x, y);
    }

    public static double loopIndex(double i, int upper) {
        return loopIndex(i, upper, 0);
    }

    public static double loopIndex(double i, int upper, int lower) {
        if (i >= lower && i < upper) {
            return i;
        }
        double range = upper - lower;
        return ((i - lower) % range + range) % range + lower;
    }
}
