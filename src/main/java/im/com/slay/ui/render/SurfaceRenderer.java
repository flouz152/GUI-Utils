package im.com.slay.ui.render;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;

/**
 * Abstraction over the rendering surface. In the actual Labymod integration
 * this would wrap the OpenGL context. The interface is intentionally expressive
 * to enable custom render backends such as shader based surfaces or CPU driven
 * vector renderers used for editor tooling.
 */
public interface SurfaceRenderer {

    void saveState();

    void restoreState();

    void translate(double x, double y, double z);

    void scale(double x, double y, double z);

    void drawRect(Rect rect, int color, double cornerRadius);

    void drawCircle(Rect rect, int color, double scale);

    void drawImage(Rect rect, String resourceLocation, double opacity);

    void drawText(String text, Vec2 position, int color, double fontSize, double shadowStrength);

    void enqueue(RenderCommand command);
}
