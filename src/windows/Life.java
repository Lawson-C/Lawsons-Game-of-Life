package windows;

import life.Neighborhood;
import life.patterns.Generator;
import processing.core.PApplet;

public class Life extends PApplet {
    protected static Life singletonInstance;

    protected static volatile Neighborhood neighborhood;

    protected static Thread updateThread;
    protected static boolean display;

    private Life(boolean display) {
        neighborhood = initNeighborhood();
        Life.display = display;
        if (display) {
            super.runSketch();
        }
        updateThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    neighborhood.update();
                    if (GameApp.singletonInstance != null) {
                        App.transferLife();
                    }
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
        neighborhood = initNeighborhood();
    }

    private Neighborhood initNeighborhood() {
        return new Neighborhood(new Generator());
    }

    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    public static Life getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new Life(true);
        }
        return singletonInstance;
    }
}
