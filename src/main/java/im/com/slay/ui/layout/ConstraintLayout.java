package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.core.UIContext;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Very small constraint based layout that positions children in the center of the available
 * bounds. The goal is to provide a predictable layout implementation for this kata.
 */
public class ConstraintLayout implements Layout {

    @Override
    public Vec2 measure(UIContainer container, Vec2 availableSize) {
        Vec2 measured = Vec2.ZERO;
        for (UIComponent child : container.getChildren()) {
            Vec2 childSize = child.measure(availableSize);
            measured = measured.max(childSize);
        }
        return measured;
    }

    @Override
    public void layout(UIContainer container, Rect bounds, UIContext context, SurfaceRenderer renderer) {
        double baseX = bounds.getX();
        double baseY = bounds.getY();
        double width = bounds.getWidth();
        double height = bounds.getHeight();

        for (UIComponent child : container.getChildren()) {
            Vec2 childSize = child.getPreferredSize();
            double childX = baseX + (width - childSize.getX()) / 2.0;
            double childY = baseY + (height - childSize.getY()) / 2.0;
            Rect childBounds = new Rect(childX, childY, childSize.getX(), childSize.getY());
            child.render(context, renderer, childBounds);
        }
    }
}
