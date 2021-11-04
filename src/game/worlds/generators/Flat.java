package game.worlds.generators;

import game.worlds.Block;
import game.worlds.Chunk;
import game.worlds.World;
import game.worlds.blockstates.Air;
import game.worlds.blockstates.Ground;

public class Flat extends Generator {
    public Flat(int size) {
        super(size);
    }

    @Override
    public Block[][][] makeChunk(Chunk hood, World world, int cx, int cy) {
        Block[][][] data = new Block[Chunk.width][Chunk.height][Chunk.width];
        for (int x = 0; x < Chunk.width; x++) {
            for (int y = 0; y < Chunk.height; y++) {
                for (int z = 0; z < Chunk.width; z++) {
                    data[x][y][z] = y >= Chunk.height / 2 ? new Ground(hood.getApplet(), world, hood, x, y, z)
                            : new Air(hood.getApplet(), world, hood, x, y, z);
                    // finds eligible world spawn
                    if (cx == 0 && cy == 0 && x == 0 && z == 0 && y >= 2 && !(data[x][y][z] instanceof Air)
                            && data[x][y - 1][z] instanceof Air && data[x][y - 2][z] instanceof Air) {
                        world.setSpawn(0, data[x][y][z].getCenter().y - world.getSize() / 2, 0);
                    }
                }
            }
        }
        return data;
    }
}
