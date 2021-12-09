package game.character;

import game.worlds.Block;
import game.worlds.World;
import game.worlds.blockstates.Air;
import processing.core.PApplet;
import processing.core.PVector;
import util.lambdas.MousePress;
import windows.GameApp;

public class Player {
    public static final int range = 5;

    protected EpicCam cam;
    protected GameApp game;
    protected World world;

    public Player(GameApp game) {
        this(game, null);
    }

    public Player(GameApp game, PVector spawn) {
        if (spawn == null) {
            this.cam = new EpicCam(game);
        } else {
            this.cam = new EpicCam(game, spawn);
        }
        this.game = game;
        this.world = game.getWorld();
        this.game.addPressHandle((MousePress) this::onPress);
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

    public PVector lookAngle() {
        return this.cam.lookAngle();
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
        float bFactor = 1.f;
        look.mult(Block.size / bFactor);
        PVector pos = this.cam.camPos();
        for (float h = 0; h <= range; h += 1 / bFactor) {
            pos.add(look);
            Block b = this.world.getBlockRaw(pos);
            if (h == range || !(b instanceof Air)) {
                return b;
            }
        }
        throw new NullPointerException("check Player.java line 68 for-loop probably");
    }

    public void onPress(int button) {
        Block b = this.targetBlock();
        PVector p = b.getCenter();
        switch (button) {
            case 37:
                this.world.placeBlockRaw(p.x, p.y, p.z, "Air");
                break;
            case 39:
                this.world.placeBlockRaw(p.x, p.y, p.z, "Ground");
                break;
        }
    }
}
