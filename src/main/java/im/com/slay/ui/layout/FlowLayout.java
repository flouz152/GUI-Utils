package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.core.UIContext;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Flow layout positions components one below the other using their preferred size.
 */
public class FlowLayout implements Layout {

    private final double spacing;

    public FlowLayout() {
        this(4.0);
    }

    public FlowLayout(double spacing) {
        this.spacing = spacing;
    }

    @Override
    public Vec2 measure(UIContainer container, Vec2 availableSize) {
        double width = 0.0;
        double height = 0.0;
        for (UIComponent child : container.getChildren()) {
            Vec2 size = child.measure(availableSize);
            width = Math.max(width, size.getX());
            if (height > 0.0) {
                height += spacing;
            }
            height += size.getY();
        }
        return new Vec2(width, height);
    }

    @Override
    public void layout(UIContainer container, Rect bounds, UIContext context, SurfaceRenderer renderer) {
        double cursorY = bounds.getY();
        for (UIComponent child : container.getChildren()) {
            Vec2 size = child.getPreferredSize();
            Rect childBounds = new Rect(bounds.getX(), cursorY, size.getX(), size.getY());
            child.onRender(context, renderer, childBounds);
            cursorY += size.getY() + spacing;
        }
    }
}
