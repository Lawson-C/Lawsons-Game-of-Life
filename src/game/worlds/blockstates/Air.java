package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;

public class Air extends Block {
    static final int low = 0;

    public Air(Chunk hood, int indx, int indy, int indz, float state) {
        super(hood, indx, indy, indz, state + low);
        Block.blockRanges.put(this.getClass(), low);
    }

    @Override
    public void display() {
        // its air you can't even see it (usually?)
    }
}
