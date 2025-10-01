package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.core.UIContext;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic flex style layout that can align children horizontally or vertically.
 */
public class FlexLayout implements Layout {

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    private final Direction direction;
    private final double spacing;

    private FlexLayout(Direction direction, double spacing) {
        this.direction = direction;
        this.spacing = spacing;
    }

    public static FlexLayout horizontal() {
        return new FlexLayout(Direction.HORIZONTAL, 4.0);
    }

    public static FlexLayout vertical() {
        return new FlexLayout(Direction.VERTICAL, 4.0);
    }

    public FlexLayout spacing(double spacing) {
        return new FlexLayout(direction, spacing);
    }

    @Override
    public Vec2 measure(UIContainer container, Vec2 availableSize) {
        double width = 0.0;
        double height = 0.0;
        List<Vec2> sizes = new ArrayList<>();
        for (UIComponent child : container.getChildren()) {
            Vec2 size = child.measure(availableSize);
            sizes.add(size);
        }

        if (direction == Direction.HORIZONTAL) {
            for (Vec2 size : sizes) {
                width += size.getX();
                height = Math.max(height, size.getY());
            }
            if (!sizes.isEmpty()) {
                width += spacing * (sizes.size() - 1);
            }
        } else {
            for (Vec2 size : sizes) {
                height += size.getY();
                width = Math.max(width, size.getX());
            }
            if (!sizes.isEmpty()) {
                height += spacing * (sizes.size() - 1);
            }
        }
        return new Vec2(width, height);
    }

    @Override
    public void layout(UIContainer container, Rect bounds, UIContext context, SurfaceRenderer renderer) {
        double cursorX = bounds.getX();
        double cursorY = bounds.getY();

        for (UIComponent child : container.getChildren()) {
            Vec2 size = child.getPreferredSize();
            Rect childBounds = new Rect(cursorX, cursorY, size.getX(), size.getY());
            child.render(context, renderer, childBounds);
            if (direction == Direction.HORIZONTAL) {
                cursorX += size.getX() + spacing;
            } else {
                cursorY += size.getY() + spacing;
            }
        }
    }
}
