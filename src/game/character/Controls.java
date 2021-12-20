package game.character;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PVector;
import windows.GameApp;

public class Controls {
	// sensitivity cannot be 0
	protected float vertSensitivity = 25;
	protected float horizSensitivity = 25;
	protected float tiltPower = 15;
	protected boolean vertLookLock = false;
	protected boolean horizLookLock = false;

	protected ArrayList<String> moves = new ArrayList<String>();
	protected HashMap<String, Boolean> running = new HashMap<String, Boolean>();
	protected HashMap<String, Boolean> toggle = new HashMap<String, Boolean>();
	protected HashMap<Character, String> controls = new HashMap<Character, String>();

	protected GameApp game;
	protected Robot bot;

	Controls(GameApp game) {
		try {
			this.bot = new Robot();
		} catch (AWTException e) {
			System.out.println(e + "\n" + "fuck");
		}
		this.game = game;

		this.moves.add("forward"); // 0
		this.moves.add("back"); // 1
		this.moves.add("left"); // 2
		this.moves.add("right"); // 3
		this.moves.add("lean left"); // 4
		this.moves.add("lean right"); // 5
		this.moves.add("jump"); // 6
		this.moves.add("sprint"); // 7
		this.moves.add("3rd person"); // 8
		this.moves.add("mouse lock"); // 9

		this.controls.put('w', this.moves.get(0));
		this.controls.put('s', this.moves.get(1));
		this.controls.put('a', this.moves.get(2));
		this.controls.put('d', this.moves.get(3));
		this.controls.put('q', this.moves.get(4));
		this.controls.put('e', this.moves.get(5));
		this.controls.put(' ', this.moves.get(6));
		this.controls.put('	', this.moves.get(7));
		this.controls.put('z', this.moves.get(8));
		this.controls.put('l', this.moves.get(9));

		this.running.put(this.moves.get(0), false);
		this.running.put(this.moves.get(1), false);
		this.running.put(this.moves.get(2), false);
		this.running.put(this.moves.get(3), false);
		this.running.put(this.moves.get(4), false);
		this.running.put(this.moves.get(5), false);
		this.running.put(this.moves.get(6), false);
		this.running.put(this.moves.get(7), false);
		this.running.put(this.moves.get(8), false);
		this.running.put(this.moves.get(9), true);

		this.toggle.put(this.moves.get(0), false);
		this.toggle.put(this.moves.get(1), false);
		this.toggle.put(this.moves.get(2), false);
		this.toggle.put(this.moves.get(3), false);
		this.toggle.put(this.moves.get(4), false);
		this.toggle.put(this.moves.get(5), false);
		this.toggle.put(this.moves.get(6), false);
		this.toggle.put(this.moves.get(7), false);
		this.toggle.put(this.moves.get(8), true);
		this.toggle.put(this.moves.get(9), true);

		this.game.addPressHandle(this::keyPressed);
		this.game.addReleaseHandle(this::keyReleased);
	}

	public void keyPressed(Character key) {
		key = Character.toLowerCase(key);
		try {
			if (this.toggle.get(this.controls.get(key))) {
				this.running.put(this.controls.get(key), !this.running.get(this.controls.get(key)));
			} else {
				this.running.put(this.controls.get(key), true);
			}
		} catch (NullPointerException e) {
		}
	}

	public void keyReleased(Character key) {
		key = Character.toLowerCase(key);
		try {
			if (!toggle.get(this.controls.get(key)) && !this.controls.get(key).equals("jump")) {
				this.running.put(this.controls.get(key), false);
			}
		} catch (NullPointerException e) {
		}
	}

	public PVector getRotation() {
		PVector diff = new PVector();
		if (this.running.get("mouse lock")) {
			if (!this.horizLookLock && this.game.mouseX > 0) {
				diff.add((float) Math
						.toRadians((this.game.mouseX - this.game.width / 2.) * (this.horizSensitivity / 100.)),
						0);
			}
			if (!this.vertLookLock && this.game.mouseY > 0) {
				diff.add(0, -(float) Math
						.toRadians((this.game.mouseY - this.game.height / 2.) * (this.vertSensitivity / 100.)));
			}
			if (this.game.isFocused()) {
				this.bot.mouseMove(2 * 1920 / 5, 2 * 1080 / 5);
			}
		} else {
			if (!this.horizLookLock && this.game.mouseX > 0) {
				diff.add((float) Math
						.toRadians((this.game.mouseX - this.game.pmouseX) * (this.horizSensitivity / 100.)), 0);
			}
			if (!this.vertLookLock && this.game.mouseY > 0) {
				diff.add(0, -(float) Math
						.toRadians((this.game.mouseY - this.game.pmouseY) * (this.vertSensitivity / 100.)));
			}
		}
		return diff;
	}

	public float getTilt() {
		if (running.get("lean left")) {
			return this.tiltPower;
		} else if (running.get("lean right")) {
			return -this.tiltPower;
		} else {
			return 0;
		}
	}

	public void setJump(boolean b) {
		this.running.put("jump", b);
	}

	public boolean getJump() {
		return this.running.get("jump");
	}

	public float jumpPower() {
		return 30f;
	}

	public String getMove() {
		if (this.running.get("forward")) {
			return "forward";
		} else if (this.running.get("back")) {
			return "back";
		} else if (this.running.get("left")) {
			return "left";
		} else if (this.running.get("right")) {
			return "right";
		}
		return "stop";
	}

	public float power() {
		return this.running.get("sprint") ? 50 : 25;
	}

	public float getGravity() {
		return 3f;
	}

	public boolean thirdPerson() {
		return this.running.get("3rd person");
	}
}
