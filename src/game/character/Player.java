package game.character;

import game.worlds.Block;
import game.worlds.World;
import game.worlds.blockstates.Air;
import processing.core.PApplet;
import processing.core.PVector;
import windows.GameApp;

public class Player {
    protected EpicCam cam;
    protected GameApp game;
    protected World world;

    public Player(GameApp game) {
        this.cam = new EpicCam(game);
        this.game = game;
        this.world = game.getWorld();
    }

    public Player(GameApp game, PVector spawn) {
        this.cam = new EpicCam(game, spawn);
        this.game = game;
        this.world = game.getWorld();
    }

    public void periodic() {
        this.cam.periodic();
    }

    public void hud() {
        this.game.pushMatrix();
        this.game.camera();
        this.game.hint(PApplet.DISABLE_DEPTH_TEST);

        this.game.noStroke();
        this.game.fill(255, this.getTint());
        this.game.rect(-500, -500, 2 * this.game.width, 2 * this.game.height);
        this.game.hint(PApplet.ENABLE_DEPTH_TEST);
        this.game.popMatrix();
    }

    public EpicCam cam() {
        return this.cam;
    }

    public PVector lookVector() {
        return this.cam.lookVector();
    }

    public float getFOV() {
        return this.cam.getFOV();
    }

    public PVector getPos() {
        return new PVector().set(this.cam.getPos());
    }

    public void keyPressed(Character key) {
        this.cam.keyPressed(key);
    }

    public void keyReleased(Character key) {
        this.cam.keyReleased(key);
    }

    public float getTint() {
        Block b = this.world.getBlockRaw(this.cam.camPos());
        return b instanceof Air ? b.getState() * 10 : 0;
    }
}
