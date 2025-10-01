package im.com.slay.ui.events;

/**
 * Simple representation of a keyboard event. Because Labymod exposes LWJGL key
 * codes we forward the integer key code to avoid unnecessary translation.
 */
public final class KeyEvent implements UIEvent {

    public enum Type {
        PRESS,
        RELEASE,
        REPEAT
    }

    private final int keyCode;
    private final Type type;

    public KeyEvent(int keyCode, Type type) {
        this.keyCode = keyCode;
        this.type = type;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public Type getType() {
        return type;
    }
}
