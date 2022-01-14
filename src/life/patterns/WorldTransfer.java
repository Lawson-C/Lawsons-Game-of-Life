package life.patterns;

import game.worlds.Block;
import game.worlds.Chunk;
import game.worlds.World;
import game.worlds.blockstates.StateRanges;

public class WorldTransfer extends Pattern {
    protected World world;

    public WorldTransfer(World world) {
        super(World.xLen * Chunk.width, Chunk.height, World.zLen * Chunk.width);
        this.world = world;
        super.makeStateMap((x, y, z) -> {
            super.states[x][y][z] = this.mapState(x, y, z) + StateRanges.get(blockType(world.getBlock(x, y, z)));
        });
    }

    /*
     * sets state based on the state of blocks in a world (passed into constructor)
     */
    public double mapState(int x, int y, int z) {
        return this.world.getBlock(x, y, z).getState();
    }

    protected String blockType(Block b) {
        return b.getClass().toString().substring(30).trim();
    }
}