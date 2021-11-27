package windows;

import java.util.ArrayList;

import game.character.Player;
import game.worlds.World;
import game.worlds.generators.Generator;
import processing.core.PApplet;
import util.lambdas.KeyEvent;

public class GameApp extends PApplet {
    protected Player player;
    protected World world;
    protected Generator gen;
    protected int blockSize;

    protected ArrayList<KeyEvent> handlePress;
    protected ArrayList<KeyEvent> handleRelease;

    public GameApp(Generator g) {
        this.blockSize = g.getSize();
        this.gen = g;
        this.world = new World(this);
        this.handlePress = new ArrayList<KeyEvent>();
        this.handleRelease = new ArrayList<KeyEvent>();
        super.runSketch();
        // due to how processing works the rest of this constructor should be empty
    }

    public void settings() {
        fullScreen(P3D, 0);
        this.player = new Player(this, this.world.getSpawn());
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

    public void keyPressed() {
        for (KeyEvent cb : this.handlePress) {
            cb.run(key);
        }
    }

    public void keyReleased() {
        for (KeyEvent cb : this.handleRelease) {
            cb.run(key);
        }
    }

    public void addPressHandle(KeyEvent cb) {
        this.handlePress.add(cb);
    }

    public void addReleaseHandle(KeyEvent cb) {
        this.handleRelease.add(cb);
    }

    public Player getP1() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    public int getSize() {
        return this.blockSize;
    }

    public Generator getGen() {
        return this.gen;
    }
}
