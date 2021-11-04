package windows;

import life.Neighborhood;
import processing.core.PApplet;

public class Life extends PApplet{
    private Neighborhood neighborhood;

    public Life() {
        neighborhood = new Neighborhood();
        super.runSketch();
    }

    public void settings() {
        size(800, 800);
    }

    public void draw() {
        ellipse(mouseX, mouseY, 25, 25);
    }
}
