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
    protected Controls controls;
    protected GameApp game;
    protected World world;

    protected float floor;
    protected float height = 150;
    protected PVector move = new PVector();
    protected PVector position = new PVector();

    public Player(GameApp game) {
        this(game, null);
    }

    public Player(GameApp game, PVector spawn) {
        this.controls = new Controls(game);
        this.cam = spawn == null ? new EpicCam(game, this.controls) : new EpicCam(game, this.controls, spawn);
        this.position = new PVector().set(spawn).sub(0, this.height);
        this.floor = spawn.y;

        this.game = game;
        this.world = game.getWorld();
        this.game.addPressHandle((MousePress) this::onPress);
    }

    public void periodic() {
        float buffer = 0;
        while (this.world.getBlockRaw(this.footPos().add(0, buffer, 0)) instanceof Air) {
            buffer += Block.size / 2.;
        }
        this.floor = this.world.getBlockRaw(this.footPos().add(0, buffer)).getRawCoords().y;
        // strafing
        String move = this.controls.getMove();
        float power = this.controls.power();
        switch (move) {
            case "forward":
                this.moveForward(power);
                break;
            case "back":
                this.moveBack(power);
                break;
            case "left":
                this.moveLeft(power);
                break;
            case "right":
                this.moveRight(power);
                break;
            default:
                this.stop();
                break;
        }
        // jumping
        if (this.controls.getJump() && !this.midAir()) {
            this.move.y = -this.controls.jumpPower();
        }
        this.collision();
        this.position.add(this.move);
        this.cam.update(this.position);
    }

    public void collision() {
        // falling
        if (this.position.y < this.floor - this.height) {
            // nothing for now
        }
        // landing
        if (this.midAir() || this.controls.getJump()) {
            this.move.y += this.controls.getGravity();
        }
        if (this.position.y + this.move.y >= this.floor - this.height) {
            this.position.y = this.floor - this.height;
            this.controls.setJump(false);
            this.move.y = 0;
        }
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
        return new PVector().set(this.position);
    }

    public PVector footPos() {
        return this.getPos().add(0, this.height);
    }

    public float getTint() {
        Block b = this.world.getBlockRaw(this.cam.camPos());
        return b instanceof Air ? b.getState() * 10 : 0;
    }

    public Block targetBlock() {
        PVector look = this.cam.lookVector();
        float bFactor = .25f;
        look.mult(Block.size * bFactor);
        PVector pos = this.cam.getPos();
        Block b;
        for (float h = 0; h <= range; h += bFactor) {
            pos.add(look);
            b = this.world.getBlockRaw(pos);
            if (!(b instanceof Air)) {
                return b;
            }
        }
        return null;
    }

    public void onPress(int button) {
        Block b = this.targetBlock();
        PVector p = null;
        if (b != null) {
            p = b.getCenter();
        }
        switch (button) {
            case 37:
                if (b != null) {
                    this.placeBlock(p.x, p.y, p.z, "Air");
                }
                break;
            case 39:
                if (b != null) {
                    this.placeBlock(p.x, p.y, p.z, "Ground");
                }
                break;
        }
    }

    public void placeBlock(float x, float y, float z, String type) {
        Block b = this.world.getBlockRaw(x, y, z);
        if (!type.equals("Air")) {
            PVector p = this.cam.getPos()
                    .sub(b.getCenter())
                    .add(this.cam.lookVector()
                            .setMag(-b.getCenter()
                                    .dist(this.cam.getPos())));
            p.x = (p.x > p.z ? 1 : 0);
            p.z = (p.z > p.x ? 1 : 0);
            p.mult(Block.size);
            x += p.x;
            z += p.z;
        }
        this.world.placeBlockRaw(x, y, z, type);
    }

    public boolean midAir() {
        return this.position.y != this.floor - this.height;
    }

    public void moveForward(float power) {
        this.move.x = power * PApplet.sin(this.cam.lookAngle().x);
        this.move.z = -power * PApplet.cos(this.cam.lookAngle().x);
    }

    public void moveBack(float power) {
        this.move.x = -power * PApplet.sin(this.cam.lookAngle().x);
        this.move.z = power * PApplet.cos(this.cam.lookAngle().x);
    }

    public void moveLeft(float power) {
        this.move.x = -power * PApplet.cos(this.cam.lookAngle().x);
        this.move.z = -power * PApplet.sin(this.cam.lookAngle().x);
    }

    public void moveRight(float power) {
        this.move.x = power * PApplet.cos(this.cam.lookAngle().x);
        this.move.z = power * PApplet.sin(this.cam.lookAngle().x);
    }

    public void stop() {
        this.move.x = 0;
        this.move.z = 0;
    }
}
