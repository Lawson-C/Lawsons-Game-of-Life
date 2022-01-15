package game.worlds.blockstates;

import game.worlds.Block;
import game.worlds.Chunk;
import windows.GameApp;

public class Plant extends Block {
    public Plant(Chunk hood, int indx, int indy, int indz, float state) {
        super(hood, indx, indy, indz, state);
    }

    @Override
    public void display() {
        GameApp.getInstance().pushMatrix();
        GameApp.getInstance().fill(0, 127 * this.state, 0);
        GameApp.getInstance().sphereDetail(3);
        GameApp.getInstance().sphere(Block.size / 4);
        GameApp.getInstance().translate(0, Block.size / 4);
        GameApp.getInstance().box(Block.size / 8, Block.size / 2, Block.size / 8);
        GameApp.getInstance().popMatrix();
    }

    @Override
    public float viscosity() {
        return .1f;
    }
}
