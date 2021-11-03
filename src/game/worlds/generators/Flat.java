package game.worlds.generators;

import game.worlds.Block;
import game.worlds.Chunk;
import game.worlds.blockstates.Air;
import game.worlds.blockstates.Ground;

public class Flat extends Generator {
    public Flat(int size) {
        super(size);
    }

    @Override
    public Block[][][] makeChunk(Chunk hood, int cx, int cy) {
        Block[][][] data = new Block[Chunk.width][Chunk.height][Chunk.width];
        for (int x = 0; x < Chunk.width; x++) {
            for (int y = 0; y < Chunk.height; y++) {
                for (int z = 0; z < Chunk.width; z++) {
                    data[x][y][z] = y >= Chunk.height / 2 ? new Ground(hood.getApplet(), this.size, hood, x, y, z)
                            : new Air(hood.getApplet(), this.size, hood, x, y, z);
                }
            }
        }
        return data;
    }
}
