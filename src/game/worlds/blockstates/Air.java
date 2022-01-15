package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;

public class Air extends Block {
    public Air(Chunk hood, int indx, int indy, int indz, float state) {
        super(hood, indx, indy, indz, state);
    }

    @Override
    public void display() {
    }

    @Override
    public float viscosity() {
        return 0;
    }
}
