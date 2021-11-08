package life.patterns;

public class Random implements Pattern {
    public float getState(int x, int y, int z, int low, int high) {
        return (float) Math.random() * (high - low) + low;
    }
}