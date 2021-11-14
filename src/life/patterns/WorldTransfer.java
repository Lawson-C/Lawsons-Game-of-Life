package life.patterns;

import game.worlds.Chunk;
import game.worlds.World;

public class WorldTransfer extends Pattern {
    protected World world;

    public WorldTransfer(World world) {
        super(world.getSize()[0] * Chunk.width, Chunk.height, world.getSize()[1] * Chunk.width);
        this.world = world;
        super.makeStateMap();
    }

    /*
     * sets state based on the state of blocks in a world (passed into constructor)
     */
    public double mapState(int x, int y, int z) {
        return this.world.getBlock(x, y, z).getState();
    }
}