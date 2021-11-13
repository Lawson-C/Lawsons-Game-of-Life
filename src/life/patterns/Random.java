package life.patterns;

public class Random implements Pattern {
    public float getState(int x, int y, int z) {
        return (float) Math.random();
    }
}