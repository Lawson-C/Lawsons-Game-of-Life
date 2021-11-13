package life;

import processing.core.PVector;
import processing.core.PApplet;

public class Cell {
    static final int SIZE = 20;

    private double state;
    private int[] index;
    private PVector pos;
    private PApplet window;

    public Cell(PApplet window, int indx, int indy, int indz, float state) {
        this.window = window;
        this.index = new int[] { indx, indy, indz };
        this.state = state;
        this.pos = new PVector(indx * SIZE, indy * SIZE, indz * SIZE);
    }

    public void display() {
        int x = this.index[0] * SIZE;
        int y = this.index[1] * SIZE;
        int z = this.index[2] * SIZE;

        this.window.pushMatrix();
        this.window.translate(x + SIZE / 2 + this.window.width / 2, y + SIZE / 2 + this.window.height / 2,
                z + SIZE / 2);
        this.window.noStroke();
        this.window.fill((float) this.state * 255, (float) this.state * 255);
        this.window.box(SIZE);
        this.window.popMatrix();
    }

    public PVector getPos() {
        return new PVector().set(pos); // don't want to return the pointer
    }

    public int[] getIndex() {
        return index;
    }

    public double getState() {
        return state;
    }

    public void setState(double state) {
        if (state > 1)
            state = 1;
        if (state < 0)
            state = 0;
        this.state = state;
    }

    public void addState(double inc) {
        this.setState(this.state + inc);
    }
}
