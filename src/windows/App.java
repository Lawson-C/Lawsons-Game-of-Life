package windows;

import game.worlds.World;
import life.Cell;
import life.Neighborhood;

public class App {
    public static void main(String[] args) {
        GameApp.getInstance();
        Life.getInstance();
    }

    public static void transferLife() {
        World w = GameApp.getInstance().getWorld();
        Neighborhood n = Life.getInstance().getNeighborhood();
        n.forEach((x, y, z) -> {
            Cell source = n.getCell(x, y, z);
            w.getBlock(x, y, z).updateState((float) source.relState());
        });
    }
}
