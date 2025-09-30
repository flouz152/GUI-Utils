package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.geometry.Vec2;

/**
 * Shared contract for all layout algorithms. Returning the measured size allows
 * layout to be chained and composed similar to CSS layout engines.
 */
public interface UILayout {
    Vec2 measure(UIContainer container, Vec2 availableSize);

    void layout(UIContainer container);

    LayoutConstraints constraintsFor(UIComponent component, UIContainer container);
}
