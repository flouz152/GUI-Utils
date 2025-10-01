package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.layout.Layout;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for components that hold children. The container delegates the layouting to a
 * {@link Layout} implementation.
 */
public class UIContainer extends UIComponent {

    private final List<UIComponent> children = new ArrayList<>();
    private Layout layout;

    public UIContainer() {
    }

    public UIContainer(Layout layout) {
        this.layout = layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Layout getLayout() {
        return layout;
    }

    public void addChild(UIComponent component) {
        children.add(component);
    }

    public void removeChild(UIComponent component) {
        children.remove(component);
    }

    public List<UIComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        if (layout == null) {
            Vec2 max = Vec2.ZERO;
            for (UIComponent child : children) {
                Vec2 measured = child.measure(availableSize);
                max = max.max(measured);
            }
            return max;
        }
        return layout.measure(this, availableSize);
    }

    @Override
    public void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        if (layout != null) {
            layout.layout(this, rect, context, renderer);
            return;
        }

        for (UIComponent child : children) {
            child.render(context, renderer, rect);
        }
    }

    public void clearChildren() {
        children.clear();
    }
}
