package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;

public class Ground extends Block {
    public Ground(Chunk hood, int indx, int indy, int indz, float state) {
        super(hood, indx, indy, indz, state);
    }

    @Override
    public void display() {
        super.game.noStroke();
        super.game.fill(10 * super.state);
        super.game.box(super.size);
    }
}
