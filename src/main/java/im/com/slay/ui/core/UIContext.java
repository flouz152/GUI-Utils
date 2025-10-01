package im.com.slay.ui.core;

/**
 * Simple context passed during rendering. It currently holds the frame time in milliseconds.
 */
public final class UIContext {

    private final long frameTimeMs;

    public UIContext(long frameTimeMs) {
        this.frameTimeMs = frameTimeMs;
    }

    public long getFrameTimeMs() {
        return frameTimeMs;
    }
}
