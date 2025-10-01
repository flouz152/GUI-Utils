package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.geometry.Vec2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constraint-based layout reminiscent of Android's ConstraintLayout. Allows
 * positioning elements relative to each other using anchors.
 */
public final class ConstraintLayout implements UILayout {

    public static final class Constraints {

        private String anchorTo;
        private double alignLeft = Double.NaN;
        private double alignTop = Double.NaN;
        private double alignRight = Double.NaN;
        private double alignBottom = Double.NaN;
        private double marginLeft;
        private double marginTop;
        private double marginRight;
        private double marginBottom;
        private double width = Double.NaN;
        private double height = Double.NaN;

        public Constraints anchor(String id) {
            this.anchorTo = id;
            return this;
        }

        public Constraints alignLeft(double offset) {
            this.alignLeft = offset;
            return this;
        }

        public Constraints alignTop(double offset) {
            this.alignTop = offset;
            return this;
        }

        public Constraints alignRight(double offset) {
            this.alignRight = offset;
            return this;
        }

        public Constraints alignBottom(double offset) {
            this.alignBottom = offset;
            return this;
        }

        public Constraints margins(double left, double top, double right, double bottom) {
            this.marginLeft = left;
            this.marginTop = top;
            this.marginRight = right;
            this.marginBottom = bottom;
            return this;
        }

        public Constraints size(double width, double height) {
            this.width = width;
            this.height = height;
            return this;
        }
    }

    public static Constraints constraints() {
        return new Constraints();
    }

    private final Map<String, UIComponent> idMap = new HashMap<String, UIComponent>();
    private final Map<UIComponent, Constraints> customConstraints = new HashMap<UIComponent, Constraints>();

    public void setConstraints(UIComponent component, Constraints constraints) {
        if (component == null || constraints == null) {
            return;
        }
        customConstraints.put(component, constraints);
    }

    @Override
    public Vec2 measure(UIContainer container, Vec2 availableSize) {
        return availableSize;
    }

    @Override
    public void layout(UIContainer container) {
        idMap.clear();
        List<UIComponent> children = container.getChildren();
        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            String id = child.getId();
            if (id != null) {
                idMap.put(id, child);
            }
        }
        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            Constraints c = customConstraints.get(child);
            Vec2 measured = child.getPreferredSize();
            if (measured.equals(Vec2.ZERO)) {
                measured = child.onMeasure(container.getPreferredSize());
            }
            double width = !Double.isNaN(c != null ? c.width : Double.NaN) ? c.width : measured.getX();
            double height = !Double.isNaN(c != null ? c.height : Double.NaN) ? c.height : measured.getY();
            Vec2 position = resolvePosition(c, width, height);
            child.setPreferredSize(new Vec2(width, height));
            child.setPosition(position);
        }
    }

    private Vec2 resolvePosition(Constraints c, double width, double height) {
        double x = 0;
        double y = 0;
        if (c != null && c.anchorTo != null) {
            UIComponent anchor = idMap.get(c.anchorTo);
            if (anchor != null) {
                Vec2 anchorPos = anchor.getPosition();
                Vec2 anchorSize = anchor.getPreferredSize();
                if (!Double.isNaN(c.alignLeft)) {
                    x = anchorPos.getX() + c.alignLeft + c.marginLeft;
                } else if (!Double.isNaN(c.alignRight)) {
                    x = anchorPos.getX() + anchorSize.getX() - width - c.alignRight - c.marginRight;
                }
                if (!Double.isNaN(c.alignTop)) {
                    y = anchorPos.getY() + c.alignTop + c.marginTop;
                } else if (!Double.isNaN(c.alignBottom)) {
                    y = anchorPos.getY() + anchorSize.getY() - height - c.alignBottom - c.marginBottom;
                }
            }
        }
        x += c != null ? c.marginLeft : 0;
        y += c != null ? c.marginTop : 0;
        return new Vec2(x, y);
    }

    @Override
    public LayoutConstraints constraintsFor(UIComponent component, UIContainer container) {
        return container.getConstraints(component);
    }
}
