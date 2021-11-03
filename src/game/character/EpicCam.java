package game.character;

import java.awt.AWTException;
import java.awt.Robot;

import processing.core.PApplet;
import processing.core.PVector;
import windows.GameApp;

class EpicCam {
	Robot bot;
	GameApp game;
	Controls controls;

	float kHeight = 150;
	float floor;

	PVector move = new PVector();
	PVector position = new PVector();
	PVector look = new PVector();
	PVector camTran = new PVector();

	public EpicCam(GameApp game) {
		this.game = game;

		try {
			this.bot = new Robot();
		} catch (AWTException e) {
			System.out.println("error with java.awt.robot class");
		}
		this.position = new PVector();
		this.floor = this.game.height;

		this.controls = new Controls(this.game);
	}

	public void periodic() {
		this.mouseMove();
		this.run();
	}

	public void update() {
		if (this.position.y > this.floor - this.kHeight) {
			this.position.y = this.floor - this.kHeight;
			this.controls.setJumping(false);
		}
		if (this.controls.getJumping()) {
			this.move.y += this.controls.getGravity();
		} else {
			this.move.y = 0;
			this.position.y = this.floor - this.kHeight;
			this.controls.setRunning("jump", false);
		}
		this.position.add(this.move);
		this.game.camera(this.position.x + this.camTran.x, this.position.y + this.camTran.y,
				this.position.z + this.camTran.z, this.position.x, this.position.y, this.position.z - 10, 0, 1, 0);
		this.frust(PApplet.radians(70));

		this.game.translate(this.position.x, this.position.y, this.position.z);
		this.game.rotateX(PApplet.radians(this.look.y));
		this.game.rotateZ(PApplet.radians(this.look.z));
		this.game.rotateY(PApplet.radians(this.look.x));
		this.game.translate(-this.position.x, -this.position.y, -this.position.z);
		crossHair();
	}

	public void mouseMove() {
		if (this.controls.getRunning("mouse lock")) {
			if (!this.controls.getHorizLock() && this.game.mouseX > 0) {
				this.look.x += (this.game.mouseX - this.game.width / 2.) * (this.controls.getHorizSense() / 100.);
			}
			if (!this.controls.getVertLock() && this.game.mouseY > 0) {
				this.look.y -= (this.game.mouseY - this.game.height / 2.) * (this.controls.getVertSense() / 100.);
			}
			if (this.look.y < -90) {
				this.look.y = -90;
			}
			if (this.look.y > 90) {
				this.look.y = 90;
			}
			if (this.game.isFocused()) {
				this.bot.mouseMove(2 * 1920 / 5, 2 * 1080 / 5);
			}
		} else {
			if (!this.controls.getHorizLock() && this.game.mouseX > 0) {
				this.look.x += (this.game.mouseX - this.game.pmouseX) * (this.controls.getHorizSense() / 100.);
			}
			if (!this.controls.getVertLock() && this.game.mouseY > 0) {
				this.look.y -= (this.game.mouseY - this.game.pmouseY) * (this.controls.getVertSense() / 100.);
			}
			if (this.look.y < -90) {
				this.look.y = -90;
			}
			if (this.look.y > 90) {
				this.look.y = 90;
			}
		}
	}

	void frust(float fov) {
		float x, y, z = (float) .01;
		x = z / PApplet.tan(fov / 2);
		y = (9 * x) / 16;
		this.game.frustum(-x, x, -y, y, z, 10000);
	}

	void crossHair() {
		this.game.pushMatrix();
		this.game.translate(
				this.position.x + this.camTran.x
						+ 2 * PApplet.cos(PApplet.radians(this.look.y)) * PApplet.sin(PApplet.radians(this.look.x)),
				this.position.y + this.camTran.y - 2 * PApplet.sin(PApplet.radians(this.look.y)),
				this.position.z + this.camTran.z
						- 2 * PApplet.cos(PApplet.radians(this.look.y)) * PApplet.cos(PApplet.radians(this.look.x)));
		this.game.strokeWeight(5);
		this.game.stroke(0, 255, 0);
		this.game.line(0, 0, 0, (float) -.15);
		this.game.stroke(255, 0, 0);
		this.game.line(0, 0, (float) .15, 0);
		this.game.stroke(0, 0, 255);
		this.game.line(0, 0, 0, 0, 0, (float) -.15);
		this.game.popMatrix();
	}

	public void run() {
		if (this.controls.getRunning("forward")) {
			this.move.x = this.controls.getPower() * PApplet.sin(PApplet.radians(this.look.x));
			this.move.z = -this.controls.getPower() * PApplet.cos(PApplet.radians(this.look.x));
		} else if (this.controls.getRunning("back")) {
			this.move.x = -this.controls.getPower() * PApplet.sin(PApplet.radians(this.look.x));
			this.move.z = this.controls.getPower() * PApplet.cos(PApplet.radians(this.look.x));
		} else if (this.controls.getRunning("left")) {
			this.move.x = -this.controls.getPower() * PApplet.cos(PApplet.radians(this.look.x));
			this.move.z = -this.controls.getPower() * PApplet.sin(PApplet.radians(this.look.x));
		} else if (this.controls.getRunning("right")) {
			this.move.x = this.controls.getPower() * PApplet.cos(PApplet.radians(this.look.x));
			this.move.z = this.controls.getPower() * PApplet.sin(PApplet.radians(this.look.x));
		}
		if (this.controls.getRunning("jump") && !this.controls.getJumping()) {
			this.controls.setJumping(true);
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

	public void keyPressed(Character key) {
		this.controls.keyPressed(key);
	}

	public void keyReleased(Character key) {
		this.controls.keyReleased(key);
	}
}
