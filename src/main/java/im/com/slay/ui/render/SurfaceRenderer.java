package im.com.slay.ui.render;

import im.com.slay.ui.geometry.Rect;

/**
 * Minimal rendering abstraction used by the UI components.
 */
public interface SurfaceRenderer {

    void fillRect(Rect rect, int color);
}
