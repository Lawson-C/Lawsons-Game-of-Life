package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;
import game.worlds.World;
import processing.core.PApplet;

public class Ground extends Block {
    public Ground(PApplet game, World world, Chunk hood, int indx, int indy, int indz) {
        super(game, world, world.getSize(), hood, indx, indy, indz);
    }

    @Override
    public void display() {
        super.game.stroke(0);
        super.game.strokeWeight(1);
        super.game.fill(205);
        super.game.box(super.size);
    }
}
