package windows;

import life.Neighborhood;
import life.patterns.WorldTransfer;
import processing.core.PApplet;

public class Life extends PApplet {
    protected volatile Neighborhood neighborhood;

    protected Thread updateThread;
    protected boolean display;

    public Life(boolean display) {
        neighborhood = new Neighborhood(this, new WorldTransfer(App.game.getWorld()));
        this.display = display;
        if (display) {
            super.runSketch();
        }
        updateThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    neighborhood.update();
                    App.transferLife();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        println(e);
                    }
                }
            }
        });
        updateThread.start();
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

        neighborhood.display();
    }

    public void mousePressed() {
        this.neighborhood = new Neighborhood(this, new WorldTransfer(App.game.getWorld()));
    }

    public Neighborhood getNeighborhood() {
        return this.neighborhood;
    }
}
