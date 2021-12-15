package game.character;

import java.awt.AWTException;
import java.awt.Robot;

import processing.core.PApplet;
import processing.core.PVector;
import windows.GameApp;

public class EpicCam {
	private Robot bot;
	private GameApp game;
	private Controls controls;

	private float kHeight = 150;
	private float floor;
	private float fov;

	private PVector move = new PVector();
	private PVector position = new PVector();
	private PVector look = new PVector();
	private PVector camTran = new PVector();

	public EpicCam(GameApp game) {
		this(game, new PVector(0, game.height));
	}

	public EpicCam(GameApp game, PVector floor) {
		this.game = game;

		try {
			this.bot = new Robot();
		} catch (AWTException e) {
			System.out.println("error with java.awt.robot class");
		}
		this.position = new PVector(floor.x, floor.y - this.kHeight, floor.z);
		this.floor = floor.y;
		this.fov = PApplet.radians(90);

		this.controls = new Controls(this.game);
	}

	public void periodic() {
		this.handleKeys();
		this.update();
	}

	public void handleKeys() {
		this.mouseMove();
		this.run();
	}

	public void update() {
		if (this.position.y > this.floor - this.kHeight) {
			this.position.y = this.floor - this.kHeight;
			this.controls.setRunning("jump", false);
		}
		if (this.controls.getRunning("jump")) {
			this.move.y += this.controls.getGravity();
		} else {
			this.move.y = 0;
			this.position.y = this.floor - this.kHeight;
		}
		this.position.add(this.move);
		this.game.camera(this.position.x + this.camTran.x, this.position.y + this.camTran.y,
				this.position.z + this.camTran.z, this.position.x, this.position.y, this.position.z - 10, 0, 1, 0);
		this.frust();

		this.game.translate(this.position.x, this.position.y, this.position.z);
		this.game.rotateX(this.look.y);
		this.game.rotateZ(this.look.z);
		this.game.rotateY(this.look.x);
		this.game.translate(-this.position.x, -this.position.y, -this.position.z);
		crossHair();
	}

	public void run() {
		if (this.controls.getRunning("forward")) {
			this.move.x = this.controls.getPower() * PApplet.sin(this.look.x);
			this.move.z = -this.controls.getPower() * PApplet.cos(this.look.x);
		} else if (this.controls.getRunning("back")) {
			this.move.x = -this.controls.getPower() * PApplet.sin(this.look.x);
			this.move.z = this.controls.getPower() * PApplet.cos(this.look.x);
		} else if (this.controls.getRunning("left")) {
			this.move.x = -this.controls.getPower() * PApplet.cos(this.look.x);
			this.move.z = -this.controls.getPower() * PApplet.sin(this.look.x);
		} else if (this.controls.getRunning("right")) {
			this.move.x = this.controls.getPower() * PApplet.cos(this.look.x);
			this.move.z = this.controls.getPower() * PApplet.sin(this.look.x);
		}
		if (this.controls.getRunning("jump") && this.position.y + this.kHeight >= this.floor && this.move.y == 0) {
			this.move.y = -this.controls.getJumpPower();
		}
		if (this.controls.getRunning("lean left")) {
			this.look.z = this.controls.getTiltPower();
		} else if (this.controls.getRunning("lean right")) {
			this.look.z = -this.controls.getTiltPower();
		}
		if (!this.controls.getRunning("forward") && !this.controls.getRunning("back")
				&& !this.controls.getRunning("left") && !this.controls.getRunning("right")) {
			this.move.x = 0;
			this.move.z = 0;
		}
		if (!this.controls.getRunning("lean left") && !this.controls.getRunning("lean right")) {
			this.look.z = 0;
		}
		if (this.controls.getRunning("sprint")) {
			controls.setPower(50);
		} else {
			controls.setPower(25);
		}
		if (this.controls.getRunning("3rd person")) {
			this.camTran.set(0, 0, this.kHeight);
		} else {
			this.camTran.set(0, 0, 0);
		}
	}

	public void mouseMove() {
		while (look.x < 0) {
			look.x += Math.PI * 2;
		}
		look.x %= Math.PI * 2;
		if (this.controls.getRunning("mouse lock")) {
			if (!this.controls.getHorizLock() && this.game.mouseX > 0) {
				this.look.x += Math
						.toRadians((this.game.mouseX - this.game.width / 2.) * (this.controls.getHorizSense() / 100.));
			}
			if (!this.controls.getVertLock() && this.game.mouseY > 0) {
				this.look.y -= Math
						.toRadians((this.game.mouseY - this.game.height / 2.) * (this.controls.getVertSense() / 100.));
			}
			if (this.look.y < -Math.PI / 2.) {
				this.look.y = (float) (-Math.PI / 2.);
			}
			if (this.look.y > Math.PI / 2.) {
				this.look.y = (float) (Math.PI / 2.);
			}
			if (this.game.isFocused()) {
				this.bot.mouseMove(2 * 1920 / 5, 2 * 1080 / 5);
			}
		} else {
			if (!this.controls.getHorizLock() && this.game.mouseX > 0) {
				this.look.x += Math
						.toRadians((this.game.mouseX - this.game.pmouseX) * (this.controls.getHorizSense() / 100.));
			}
			if (!this.controls.getVertLock() && this.game.mouseY > 0) {
				this.look.y -= Math
						.toRadians((this.game.mouseY - this.game.pmouseY) * (this.controls.getVertSense() / 100.));
			}
			if (this.look.y < -Math.PI / 2.) {
				this.look.y = (float) (-Math.PI / 2.);
			}
			if (this.look.y > Math.PI / 2.) {
				this.look.y = (float) (Math.PI / 2.);
			}
		}
	}

	void frust() {
		float x, y, z = (float) .01;
		x = z * PApplet.tan(this.fov / 2);
		y = (9 * x) / 16;
		this.game.frustum(-x, x, -y, y, z, 10000);
	}

	void crossHair() {
		this.game.pushMatrix();
		PVector crossPos = this.lookVector().mult(2);
		this.game.translate(this.position.x + this.camTran.x + crossPos.x,
				this.position.y + this.camTran.y + crossPos.y,
				this.position.z + this.camTran.z + crossPos.z);
		this.game.strokeWeight(5);
		this.game.stroke(0, 255, 0);
		this.game.line(0, 0, 0, (float) -.15);
		this.game.stroke(255, 0, 0);
		this.game.line(0, 0, (float) .15, 0);
		this.game.stroke(0, 0, 255);
		this.game.line(0, 0, 0, 0, 0, (float) .15);
		this.game.popMatrix();
		this.game.noCursor();
	}

	public PVector lookAngle() {
		return new PVector().set(this.look);
	}

	public PVector lookVector() {
		return new PVector(PApplet.cos(this.look.y) * PApplet.sin(this.look.x),
				-PApplet.sin(this.look.y),
				-PApplet.cos(this.look.y) * PApplet.cos(this.look.x));
	}

	public PVector getPos() {
		return this.position;
	}

	public PVector camPos() {
		return new PVector().set(this.position).add(this.camTran);
	}

	public float getHeight() {
		return this.kHeight;
	}

	public float getFOV() {
		return this.fov;
	}
}
