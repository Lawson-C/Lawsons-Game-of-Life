package windows;

import game.worlds.World;
import game.worlds.generators.Flat;
import life.Cell;
import life.Neighborhood;

public class App {
    public static GameApp game;
    public static Life life;

    public static void main(String[] args) {
        game = new GameApp(new Flat(100));
        life = new Life(false);
    }

    public static void transferLife() {
        World w = game.getWorld();
        Neighborhood n = life.getNeighborhood();
        n.forEach((x, y, z) -> {
            Cell source = n.getCell(x, y, z);
            w.getBlock(x, y, z).updateState((float) source.relState());
        });
    }
}
