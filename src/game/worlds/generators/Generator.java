package game.worlds.generators;

import game.worlds.Block;
import game.worlds.Chunk;
import game.worlds.World;

public abstract class Generator {
    protected int size;

    Generator(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    // generates a chunk's contents based on its index in a grid and its pointer
    public abstract Block[][][] makeChunk(Chunk hood, World world, int x, int y);
}
