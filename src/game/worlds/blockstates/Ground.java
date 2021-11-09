package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;

public class Ground extends Block {
    static final int low = 1;

    public Ground(Chunk hood, int indx, int indy, int indz, float state) {
        super(hood, indx, indy, indz, state + low);
        Block.blockRanges.put(this.getClass(), low);
    }

    @Override
    public void display() {
        super.game.stroke(0);
        super.game.strokeWeight(1);
        super.game.fill(205);
        super.game.box(super.size);
    }
}
