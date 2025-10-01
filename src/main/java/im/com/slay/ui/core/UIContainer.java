package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.layout.LayoutConstraints;
import im.com.slay.ui.layout.UILayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for components that manage a layout algorithm. Containers expose
 * a familiar API reminiscent of CSS flexbox or grid containers and are
 * intended to be composed with concrete layout managers such as
 * {@link im.com.slay.ui.layout.FlexLayout} or {@link im.com.slay.ui.layout.GridLayout}.
 */
public abstract class UIContainer extends UIComponent {

    private final Map<UIComponent, LayoutConstraints> constraints = new HashMap<UIComponent, LayoutConstraints>();
    private UILayout layout;

    public UIContainer(UILayout layout) {
        this.layout = layout;
    }

    public void setLayout(UILayout layout) {
        this.layout = layout;
        invalidateLayout();
    }

    public void setConstraints(UIComponent component, LayoutConstraints constraint) {
        constraints.put(component, constraint);
        invalidateLayout();
    }

    public LayoutConstraints getConstraints(UIComponent component) {
        LayoutConstraints value = constraints.get(component);
        return value == null ? LayoutConstraints.none() : value;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        if (layout == null) {
            return availableSize;
        }
        return layout.measure(this, availableSize);
    }

    @Override
    protected void onLayout() {
        if (layout != null) {
            layout.layout(this);
        }
    }

    public UILayout getLayout() {
        return layout;
    }
}
