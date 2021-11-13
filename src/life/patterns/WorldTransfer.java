package life.patterns;

import game.worlds.Chunk;
import game.worlds.World;

public class WorldTransfer extends Pattern {
    protected World world;

    public WorldTransfer(World world) {
        super(world.getSize()[0], Chunk.height, world.getSize()[1]);
        this.world = world;
    }

    /*
     * sets state based on the state of blocks in a world (passed into constructor)
     */
    public double getState(int x, int y, int z) {
        return 0;
    }
}