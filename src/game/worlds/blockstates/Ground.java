package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;
import processing.core.PApplet;

public class Ground extends Block {
    public Ground(PApplet game, int size, Chunk hood, int indx, int indy, int indz) {
        super(game, size, hood, indx, indy, indz);
    }

    @Override
    public void display() {
        super.game.pushMatrix();
        super.game.translate(super.indx * super.size - super.hood.rawWidth() / 2,
                super.indy * super.size - super.hood.rawHeight() / 2, super.indz * super.size - super.hood.rawWidth() / 2);
        super.game.box(super.size);
        super.game.popMatrix();
    }
}
