package windows;

import game.worlds.generators.Flat;

public class App {
    private static GameApp game;
    private static Life life;

    public static void main(String[] args) {
        life = new Life();
        game = new GameApp(new Flat(100));
    }
}
