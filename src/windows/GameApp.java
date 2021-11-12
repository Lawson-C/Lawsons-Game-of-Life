package windows;

import game.character.Player;
import game.worlds.World;
import game.worlds.generators.Generator;
import processing.core.PApplet;

public class GameApp extends PApplet {
    protected Player player;
    protected World world;
    protected Generator gen;
    protected int blockSize;

    public GameApp(Generator g) {
        this.blockSize = g.getSize();
        this.gen = g;
        this.world = new World(this);
        super.runSketch();
        // due to how processing works the rest of this constructor should be empty
        // (place init stuff in void settings())
    }

    public void settings() {
        fullScreen(P3D, 0);
        this.player = new Player(this, this.world.getSpawn());
    }

    public void draw() {
        background(0, 55, 127);
        player.periodic();
        world.display();
        player.hud();
    }

    public boolean isFocused() {
        return super.focused;
    }

    public void keyPressed() {
        this.player.keyPressed(key);
    }

    public void keyReleased() {
        this.player.keyReleased(key);
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
