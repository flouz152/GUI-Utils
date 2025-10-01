package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;

import java.util.List;

/**
 * Stack layout layers children on top of each other which is ideal for
 * background effects and overlay animations. Components can opt-in to consume
 * pointer events exclusively which makes it trivial to build modal dialogues or
 * context menus.
 */
public final class StackLayout implements UILayout {

    private boolean cascadeSizes = true;

    public StackLayout cascadeSizes(boolean cascadeSizes) {
        this.cascadeSizes = cascadeSizes;
        return this;
    }

    @Override
    public Vec2 measure(UIContainer container, Vec2 availableSize) {
        return availableSize;
    }

    @Override
    public void layout(UIContainer container) {
        List<UIComponent> children = container.getChildren();
        Vec2 preferred = container.getPreferredSize();
        for (UIComponent child : children) {
            if (cascadeSizes) {
                child.setPreferredSize(preferred);
            }
            Rect rect = Rect.of(0, 0, preferred.getX(), preferred.getY());
            child.setPosition(rect.getPosition());
        }
    }

    @Override
    public LayoutConstraints constraintsFor(UIComponent component, UIContainer container) {
        return LayoutConstraints.none();
    }
}
