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
    protected World world;

    protected float floor;
    protected float height = 150;
    protected PVector move = new PVector();
    protected PVector position = new PVector();

    public Player() {
        this(null);
    }

    public Player(PVector spawn) {
        this.controls = new Controls();
        this.cam = spawn == null ? new EpicCam(this.controls) : new EpicCam(this.controls, spawn);
        this.position = new PVector().set(spawn).sub(0, this.height);
        this.floor = spawn.y;

        this.world = GameApp.getInstance().getWorld();
        GameApp.addPressHandle((MousePress) this::onPress);
    }

    public void periodic() {
        float buffer = 0;
        while (this.world.getBlockRaw(this.footPos().add(0, buffer, 0)).viscosity() != 1) {
            buffer += Block.size / 2.;
        }
        this.floor = this.world.getBlockRaw(this.footPos().add(0, buffer)).getRawCoords().y;
        // strafing
        float visc = 1 - this.world.getBlockRaw(this.getPos().add(0, Block.size)).viscosity();
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
        if (this.midAir() || this.controls.getJump()) {
            this.move.y += this.controls.getGravity();
        }
        if (this.controls.getJump() && !this.midAir()) {
            this.move.y = -this.controls.jumpPower();
        }
        this.move.mult(visc);
        this.collision();
        this.position.add(this.move);
        this.cam.update(this.position);
    }

    public void collision() {
        // landing
        if (this.position.y + this.move.y >= this.floor - this.height) {
            this.position.y = this.floor - this.height;
            this.controls.setJump(false);
            this.move.y = 0;
        }
        // strafing
        float buffer = 20.f;
        for (int lvl = 0; lvl <= 1; lvl++) {
            Block xBlock = this.world.getBlockRaw(
                    this.getPos().add(this.move.x + Math.signum(this.move.x) * buffer, lvl * Block.size));
            if (xBlock.viscosity() == 1) {
                this.position.x = xBlock.getCenter().x - (Block.size / 2.f + buffer) * Math.signum(this.move.x);
                this.move.x = 0;
            }
            Block zBlock = this.world.getBlockRaw(
                    this.getPos().add(0, lvl * Block.size, this.move.z + Math.signum(this.move.z) * buffer));
            if (zBlock.viscosity() == 1) {
                this.position.z = zBlock.getCenter().z - (Block.size / 2.f + buffer) * Math.signum(this.move.z);
                this.move.z = 0;
            }
        }
    }

    public void hud() {
        GameApp.getInstance().pushMatrix();
        GameApp.getInstance().camera();
        GameApp.getInstance().hint(PApplet.DISABLE_DEPTH_TEST);

        GameApp.getInstance().noStroke();
        GameApp.getInstance().fill(255, this.getTint());
        GameApp.getInstance().rect(-500, -500, 2 * GameApp.getInstance().width, 2 * GameApp.getInstance().height);
        GameApp.getInstance().hint(PApplet.ENABLE_DEPTH_TEST);
        GameApp.getInstance().popMatrix();

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
            PVector p = new PVector().set(this.position).sub(b.getCenter());
            p.add(this.cam.lookVector().setMag(p.mag()));
            // this almost worked
            double theta = Math.atan2(p.x, p.z);
            double theta2 = Math.atan2(p.y, Math.sqrt(p.x * p.x + p.z * p.z));
            theta = (int) (theta * 2.d / Math.PI) * Math.PI / 2.d;
            theta2 = (int) (theta2 * 2.d / Math.PI) * Math.PI / 2.d;
            x += (int) (Math.cos(theta2) * Math.sin(theta)) * Block.size;
            y += (int) (Math.sin(theta2)) * Block.size;
            z += (int) (Math.cos(theta2) * Math.cos(theta)) * Block.size;
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
