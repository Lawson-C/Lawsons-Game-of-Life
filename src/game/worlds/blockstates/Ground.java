package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;
import windows.GameApp;

public class Ground extends Block {
    public Ground(Chunk hood, int indx, int indy, int indz, float state) {
        super(hood, indx, indy, indz, state);
    }

    @Override
    public void display() {
        GameApp.getInstance().strokeWeight(1);
        GameApp.getInstance().stroke(0);
        GameApp.getInstance().fill(0, 10 * super.state + 50, 0);
        GameApp.getInstance().box(Block.size);
    }

    @Override
    public float viscosity() {
        return 1;
    }
}
