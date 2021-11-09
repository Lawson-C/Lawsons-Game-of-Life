package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;

public class Ground extends Block {
    public Ground(Chunk hood, int indx, int indy, int indz, float state) {
        super(hood, indx, indy, indz, state);
        Block.blockRanges.put(this.getClass(), 1);
    }

    @Override
    public void display() {
        super.game.stroke(0);
        super.game.strokeWeight(1);
        super.game.fill(205);
        super.game.box(super.size);
    }
}
