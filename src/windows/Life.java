package windows;

import life.Neighborhood;
import life.patterns.Random;
import processing.core.PApplet;

public class Life extends PApplet {
    private Neighborhood neighborhood;

    public Life() {
        neighborhood = new Neighborhood(this, new Random(), 16, 16, 16);
        super.runSketch();
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
}
