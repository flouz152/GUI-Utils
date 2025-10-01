package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Invisible spacer component to add vertical or horizontal gaps.
 */
public class UISpacer extends UIComponent {

    public UISpacer(double width, double height) {
        setPreferredSize(new Vec2(width, height));
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return getPreferredSize();
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        // Intentionally left blank.
    }
}
