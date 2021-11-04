package windows;

import game.character.Player;
import game.worlds.World;
import game.worlds.generators.Generator;
import processing.core.PApplet;

public class GameApp extends PApplet {
    Player player;
    World world;
    int blockSize;

    public GameApp(Generator g) {
        this.blockSize = g.getSize();
        this.world = new World(this, g, this.blockSize);
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
}
