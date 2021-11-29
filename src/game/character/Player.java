package game.character;

import game.worlds.Block;
import game.worlds.World;
import game.worlds.blockstates.Air;
import processing.core.PApplet;
import processing.core.PVector;
import windows.GameApp;

public class Player {
    public static final int range = 3;

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

        this.world.getChunkRaw(this.getPos()).chunkStroke();
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

    public float getTint() {
        Block b = this.world.getBlockRaw(this.cam.camPos());
        return b instanceof Air ? b.getState() * 10 : 0;
    }

    public Block targetBlock() {
        PVector look = this.cam.lookVector();
        PVector pos = this.getPos();
        for (int h = 0; h <= range; h++) {
            PVector diff = new PVector().set((float) (Block.size * Math.sqrt(3) * Math.cos(look.y) * Math.sin(look.x)),
                    (float) (-Block.size * Math.sqrt(3) * Math.sin(look.y)),
                    (float) (-Block.size * Math.sqrt(3) * Math.cos(look.y) * Math.cos(look.x)));
            pos.add(diff);
            Block b = this.world.getBlockRaw(pos);
            if (h == range || !(b instanceof Air)) {
                return b;
            }
        }
        throw new NullPointerException("check for-loop probably");
    }
}
