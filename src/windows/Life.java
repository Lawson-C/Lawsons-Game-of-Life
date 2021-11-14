package windows;

import life.Neighborhood;
import life.patterns.Random;
import life.patterns.WorldTransfer;
import processing.core.PApplet;

public class Life extends PApplet {
    protected volatile Neighborhood neighborhood;

    protected Thread noVisualsThread;
    protected boolean display;

    public Life(boolean display) {
        neighborhood = new Neighborhood(this, new WorldTransfer(App.game.getWorld()));
        this.display = display;
        if (display) {
            super.runSketch();
        } else {
            noVisualsThread = new Thread(new Runnable() {
                public void run() {
                    neighborhood.update();
                }
            });
            noVisualsThread.start();
        }
    }

    public void settings() {
        size(800, 800, P3D);
    }

    public void draw() {
        background(0, 0, 255);

        translate(width / 2, height / 2);
        rotateY(map(mouseX, 0, width, -TAU, TAU));
        rotateX(-map(mouseY, 0, height, -TAU, TAU));
        translate(-width / 2, -height / 2);

        neighborhood.update();
        neighborhood.display();
    }

    public void mousePressed() {
        this.neighborhood = new Neighborhood(this, new WorldTransfer(App.game.getWorld()));
    }
}
