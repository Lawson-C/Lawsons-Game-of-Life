package game.worlds;

import processing.core.PApplet;

public abstract class Block {
    protected PApplet game;
    protected World world;
    protected int size;
    protected Chunk hood;
    protected int indx, indy, indz; // indecies within chunk

    public Block(PApplet game, World world, int size, Chunk hood, int indx, int indy, int indz) {
        this.game = game;
        this.world = world;
        this.size = size;
        this.hood = hood;
        this.indx = indx;
        this.indy = indy;
        this.indz = indz;
    }

    public void transPos() {
        this.game.translate((this.indx + (float) .5) * this.size, (this.indy + (float) .5) * this.size,
                (this.indz + (float) .5) * this.size);
    }

    /*
     * the design of the methods to show the block on screen is assures that the
     * matrix is pushed and popped each time and that the block subclasses only have
     * to worry about their visual style, not their coordinates.
     * 
     * All this because I can't trust my future self...
     */
    public void blockMatrix() {
        this.game.pushMatrix();
        transPos();
        display();
        this.game.popMatrix();
    }

    /*
     * Chunk 'hood' must call translate beforehand
     */
    public abstract void display();
}
