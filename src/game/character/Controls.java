package game.character;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PConstants;
import windows.GameApp;

public class Controls {
  GameApp game;

  // sensitivity cannot be 0
  private float vertSensitivity = 25;
  private float horizSensitivity = 25;
  private float power = 25;
  private float tiltPower = 15;
  private float jumpPower = 20;
  private float gravity = 1;
  private boolean vertLookLock = false;
  private boolean horizLookLock = false;
  private boolean jumping = false;

  private ArrayList<String> moves = new ArrayList<String>();
  private HashMap<String, Boolean> running = new HashMap<String, Boolean>();
  private HashMap<String, Boolean> toggle = new HashMap<String, Boolean>();
  private HashMap<Character, String> controls = new HashMap<Character, String>();

  Controls(GameApp game) {
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
    this.controls.put(PConstants.TAB, this.moves.get(7));
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
  }

  public void keyPressed(Character key) {
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
    try {
      if (!toggle.get(this.controls.get(key))) {
        this.running.put(this.controls.get(key), false);
      }
    } catch (NullPointerException e) {
    }
  }

  public float getVertSense() {
    return this.vertSensitivity;
  }

  public float getHorizSense() {
    return this.horizSensitivity;
  }

  public float getPower() {
    return this.power;
  }

  public void setPower(float p) {
    this.power = p;
  }

  public float getTiltPower() {
    return this.tiltPower;
  }

  public float getJumpPower() {
    return this.jumpPower;
  }

  public float getGravity() {
    return this.gravity;
  }

  public boolean getVertLock() {
    return this.vertLookLock;
  }

  public boolean getHorizLock() {
    return this.horizLookLock;
  }

  public boolean getJumping() {
    return this.jumping;
  }

  public void setJumping(boolean b) {
    this.jumping = b;
  }

  public void setRunning(String s, boolean b) {
    this.running.put(s, b);
  }

  public boolean getRunning(String s) {
    return this.running.get(s);
  }

  public HashMap<String, Boolean> getRunning() {
    return this.running;
  }

  public HashMap<String, Boolean> getToggle() {
    return this.toggle;
  }

  public HashMap<Character, String> getControls() {
    return this.controls;
  }
}
