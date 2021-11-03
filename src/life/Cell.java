package life;

import processing.core.PVector;
import processing.core.PApplet;

public class Cell {
    static int SIZE = 10;

    private double state;
    private int[] index;
    private PVector pos;
    private PApplet window;

    Cell(PApplet window, int indx, int indy, int indz, float state) {
        this.window = window;
        this.index = new int[] { indx, indy, indz };
        this.state = state;
        this.pos = new PVector(indx * SIZE, indy * SIZE, indz * SIZE);
    }

    public void display() {
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
        this.state = state;
    }
}
