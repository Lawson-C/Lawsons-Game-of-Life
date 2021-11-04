package game.character;

import processing.core.PVector;
import windows.GameApp;

public class Player {
    private EpicCam cam;

    public Player(GameApp game) {
        this.cam = new EpicCam(game);
    }

    public Player(GameApp game, PVector spawn) {
        this.cam = new EpicCam(game, spawn);
    }

    public void periodic() {
        this.cam.update();
        this.cam.periodic();
    }

    public EpicCam cam() {
        return this.cam;
    }

    public void keyPressed(Character key) {
        this.cam.keyPressed(key);
    }

    public void keyReleased(Character key) {
        this.cam.keyReleased(key);
    }
}
