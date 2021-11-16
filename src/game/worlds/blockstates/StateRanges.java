package game.worlds.blockstates;

public class StateRanges {
    public static final String[] keys = new String[] { "Air", "Ground" };

    public static int get(String s) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public static String get(int i) {
        return keys[i];
    }
}
