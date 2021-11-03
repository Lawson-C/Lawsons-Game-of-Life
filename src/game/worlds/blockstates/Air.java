package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;
import game.worlds.World;
import processing.core.PApplet;

public class Air extends Block {
    public Air(PApplet game, World world, Chunk hood, int indx, int indy, int indz) {
        super(game, world, world.getSize(), hood, indx, indy, indz);
    }

    @Override
    public void display() {
        // its air you can't even see it (usually?)
    }
}
