package windows;

import game.worlds.generators.Flat;

public class App {
    public static GameApp game;
    
    public static void main(String[] args) {
        game = new GameApp(new Flat(100));
    }
}
