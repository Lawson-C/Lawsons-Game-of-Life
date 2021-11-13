package util;

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
        while (x < -super.nx) x += super.nx + super.px;
        while (x >= super.px) x -= super.nx + super.px;

        while (y < -super.ny) y += super.ny + super.py;
        while (y >= super.py) y -= super.ny + super.py;

        return super.get(x, y);
    }
}
