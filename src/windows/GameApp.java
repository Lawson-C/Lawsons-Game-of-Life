package windows;

import java.util.ArrayList;

import game.character.Player;
import game.worlds.World;
import game.worlds.generators.Flat;
import game.worlds.generators.Generator;
import processing.core.PApplet;
import util.lambdas.KeyEvent;
import util.lambdas.MousePress;

public class GameApp extends PApplet {
    protected static GameApp singletonInstance;

    protected static Player player;
    protected static World world;
    protected static Generator gen;

    protected static ArrayList<KeyEvent> handlePress;
    protected static ArrayList<KeyEvent> handleRelease;
    protected static ArrayList<MousePress> handleMousePress;
    protected static ArrayList<MousePress> handleMouseRelease;

    private GameApp() {
    }

    private static void init() {
        gen = new Flat();
        world = new World();
        handlePress = new ArrayList<KeyEvent>();
        handleRelease = new ArrayList<KeyEvent>();
        handleMousePress = new ArrayList<MousePress>();
        handleMouseRelease = new ArrayList<MousePress>();
        singletonInstance.runSketch();
        // due to how processing works the rest of this method should be empty
    }

    public static GameApp getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new GameApp();
            init();
        }
        return singletonInstance;
    }

    public void settings() {
        fullScreen(P3D, 0);
        player = new Player(world.getSpawn());
    }

    public void draw() {
        background(0, 55, 127);
        player.periodic();
        world.renderBlocks();
        player.hud();
    }

    public boolean isFocused() {
        return super.focused;
    }

    // key event handlers

    public void keyPressed() {
        for (KeyEvent cb : handlePress) {
            cb.run(key);
        }
    }

    public void keyReleased() {
        for (KeyEvent cb : handleRelease) {
            cb.run(key);
        }
    }

    public static void addPressHandle(KeyEvent cb) {
        handlePress.add(cb);
    }

    public static void addReleaseHandle(KeyEvent cb) {
        handleRelease.add(cb);
    }

    // mouse event handlers

    public void mousePressed() {
        for (MousePress cb : handleMousePress) {
            cb.run(super.mouseButton);
        }
    }

    public void mouseReleased() {
        for (MousePress cb : handleMouseRelease) {
            cb.run(super.mouseButton);
        }
    }

    public static void addPressHandle(MousePress cb) {
        handleMousePress.add(cb);
    }

    public static void addReleaseHandle(MousePress cb) {
        handleMouseRelease.add(cb);
    }

    // getters

    public Player getP1() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public Generator getGen() {
        return gen;
    }
}
