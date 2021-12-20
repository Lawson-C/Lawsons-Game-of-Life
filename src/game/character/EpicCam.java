package game.character;

import processing.core.PApplet;
import processing.core.PVector;
import windows.GameApp;

public class EpicCam {
	protected GameApp game;
	protected Controls controls;

	protected float fov;
	protected PVector position = new PVector();
	protected PVector look = new PVector();
	protected PVector camTran = new PVector();

	public EpicCam(GameApp game, Controls controls) {
		this(game, controls, new PVector(0, 0, 0));
	}

	public EpicCam(GameApp game, Controls controls, PVector pos) {
		this.game = game;
		this.controls = controls;
		this.position = new PVector().set(pos);
		this.fov = PApplet.radians(110);
	}

	public void update(PVector newPos) {
		this.position.set(newPos);
		this.aim();
		this.camTran.z = this.controls.thirdPerson() ? 500 : 0;

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

	public void aim() {
		PVector diff = this.controls.getRotation();
		this.look.add(diff);
		this.regulate();
		this.look.z = this.controls.getTilt();
	}

	public void regulate() {
		while (look.x < 0) {
			look.x += Math.PI * 2;
		}
		look.x %= Math.PI * 2;
		if (this.look.y < -Math.PI / 2.) {
			this.look.y = (float) (-Math.PI / 2.);
		}
		if (this.look.y > Math.PI / 2.) {
			this.look.y = (float) (Math.PI / 2.);
		}
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
		return new PVector().set(this.position);
	}

	public PVector camPos() {
		return new PVector().set(this.position).add(this.camTran);
	}

	public float getFOV() {
		return this.fov;
	}
}
