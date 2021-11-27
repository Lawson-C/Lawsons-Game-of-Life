package game.worlds.generators;

import game.worlds.Block;
import game.worlds.Chunk;
import game.worlds.World;

public interface Generator {
    // generates a chunk's contents based on its index in a grid and its pointer
    public Block[][][] makeChunk(Chunk hood, World world, int x, int y);
}
