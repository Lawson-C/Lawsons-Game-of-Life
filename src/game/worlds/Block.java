package game.worlds;

import game.worlds.blockstates.Air;
import game.worlds.blockstates.Ground;
import game.worlds.blockstates.StateRanges;
import processing.core.PVector;
import windows.GameApp;

public abstract class Block {
    public static final int size = 100;

    protected GameApp game;
    protected World world;
    protected Chunk hood;
    protected int indx, indy, indz; // indecies within chunk
    protected PVector pos;
    protected float state;

    public Block(Chunk hood, int indx, int indy, int indz, float state) {
        this.game = hood.getApplet();
        this.world = hood.getWorld();
        this.hood = hood;
        this.indx = indx;
        this.indy = indy;
        this.indz = indz;
        this.state = state;
        this.pos = new PVector(this.indx * Block.size, this.indy * Block.size, this.indz * Block.size);
    }

    /*
     * Chunk 'hood' must call translate beforehand
     */
    public abstract void display();

    public void transPos() {
        this.game.translate(this.pos.x + Block.size / 2, this.pos.y + Block.size / 2, this.pos.z + Block.size / 2);
    }

    /*
     * the design of the methods to show the block on screen is designed to assure
     * that the matrix is pushed and popped each time and that the block subclasses
     * only have to worry about their visual style, not their coordinates.
     * 
     * All this because I can't trust my future self...
     */
    public void blockMatrix() {
        this.game.pushMatrix();
        transPos();
        display();
        this.game.popMatrix();
    }

    public void updateState(float s) {
        if (s < 0)
            s = 0;
        if (s > 1)
            s = 1;
        this.state = s;
    }

    public float getState() {
        return this.state;
    }

    public PVector getRawCoords() {
        return new PVector().set(this.hood.getRawCoords()).add(this.pos);
    }

    public PVector getCenter() {
        return this.getRawCoords().add(Block.size / 2, Block.size / 2, Block.size / 2);
    }

    public Block changeType(String type, float state) {
        switch (type) {
            case "Air":
                return new Air(this.hood, this.indx, this.indy, this.indz, state);
            case "Ground":
                return new Ground(this.hood, this.indx, this.indy, this.indz, state);
            default:
                return null;
        }
    }

    public Block changeType(String type) {
        return this.changeType(type, StateRanges.get(type));
    }
}
