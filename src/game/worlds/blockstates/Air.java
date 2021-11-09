package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;

public class Air extends Block {
    public Air(Chunk hood, int indx, int indy, int indz, float state) {
        super(hood, indx, indy, indz, state);
        Block.blockRanges.put(this.getClass(), 0);
    }

    @Override
    public void display() {
        // its air you can't even see it (usually?)
    }
}
