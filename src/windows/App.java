package windows;

import game.worlds.generators.Flat;

public class App {
    private static GameApp game;
    private static Life life;

    public static void main(String[] args) {
        game = new GameApp(new Flat(100));
        life = new Life();
    }
}
