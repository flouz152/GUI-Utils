package org.lwjgl.input;

/**
 * Minimal LWJGL keyboard stub that exposes a couple of key constants.
 */
public final class Keyboard {

    public static final int KEY_ESCAPE = 1;
    public static final int KEY_BACK = 14;
    public static final int KEY_RETURN = 28;
    public static final int KEY_LEFT = 203;
    public static final int KEY_RIGHT = 205;
    public static final int KEY_DELETE = 211;
    public static final int KEY_HOME = 199;
    public static final int KEY_END = 207;
    public static final int KEY_R = 19;

    public static boolean isKeyDown(int key) {
        return false;
    }

    private Keyboard() {
    }
}
