package life.patterns;

import game.worlds.World;

public class WorldTransfer implements Pattern {
    protected World world;

    public WorldTransfer(World world) {
        this.world = world;
    }

    /*
    * sets state based on the state of blocks in a world (passed into constructor)
    */
    public float getState(int x, int y, int z) {
        return 0;
    }
}