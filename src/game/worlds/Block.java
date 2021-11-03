package game.worlds;

import processing.core.PApplet;

public abstract class Block {
    protected PApplet game;
    protected int size;
    protected Chunk hood;
    protected int indx, indy, indz; // indexes within chunk

    public Block(PApplet game, int size, Chunk hood, int indx, int indy, int indz) {
        this.game = game;
        this.size = size;
        this.hood = hood;
        this.indx = indx;
        this.indy = indy;
        this.indz = indz;
    }

    /*
     * Chunk 'hood' must call translate beforehand
     */
    public abstract void display();
}
