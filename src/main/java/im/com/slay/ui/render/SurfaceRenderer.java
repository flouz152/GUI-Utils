package im.com.slay.ui.render;

import im.com.slay.ui.geometry.Rect;

/**
 * Minimal rendering abstraction used by the UI components.
 */
public interface SurfaceRenderer {

    void fillRect(Rect rect, int color);

    default void drawString(String text, double x, double y, int color) {
    }

    default void drawCenteredString(String text, double centerX, double y, int color) {
    }
}
