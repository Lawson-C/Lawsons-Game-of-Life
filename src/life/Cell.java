package life;

import game.worlds.blockstates.StateRanges;
import processing.core.PApplet;
import processing.core.PVector;

public class Cell {
    static final int SIZE = 5;

    protected double state;
    protected int[] index;
    protected PVector pos;
    protected PApplet window;
    protected String type;
    protected int low;

    public Cell(PApplet window, int indx, int indy, int indz, double state) {
        this.window = window;
        this.index = new int[] { indx, indy, indz };
        this.state = state;
        this.pos = new PVector(indx * SIZE, indy * SIZE, indz * SIZE);
        this.low = (int) Math.floor(state);
        this.type = StateRanges.get(this.low);
    }

    public void display() {
        int x = this.index[0] * SIZE;
        int y = this.index[1] * SIZE;
        int z = this.index[2] * SIZE;

        this.window.pushMatrix();
        this.window.translate(x + SIZE / 2 + this.window.width / 2, y + SIZE / 2 + this.window.height / 2,
                z + SIZE / 2);
        this.window.noStroke();
        this.window.fill(255 - (float) (this.state) * 127);
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
        return this.state;
    }

    public double relState() {
        return this.state - this.low;
    }

    public String getType() {
        return this.type;
    }

    public boolean sameType(Cell c) {
        return this.type.equals(c.getType());
    }

    public void setState(double state) {
        if (state < low)
            state = low;
        if (state > low + 1)
            state = low + 1;
        this.state = state;
    }

    public void addState(double inc) {
        this.setState(this.state + inc);
    }
}
