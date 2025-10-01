package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.core.UIContext;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Common interface used to measure and layout children inside a {@link UIContainer}.
 */
public interface Layout {

    Vec2 measure(UIContainer container, Vec2 availableSize);

    void layout(UIContainer container, Rect bounds, UIContext context, SurfaceRenderer renderer);

    default Vec2 measureChild(UIComponent child, Vec2 availableSize) {
        return child.measure(availableSize);
    }
}
