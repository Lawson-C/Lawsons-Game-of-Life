package life;

public class Neighborhood {
    Cell[][][] cells;

    Neighborhood() {
    }

    public void display() {
        for (Cell[][] c1 : cells) {
            for (Cell[] c2 : c1) {
                for (Cell c : c2) {
                    c.display();
                }
            }
        }
    }
}
