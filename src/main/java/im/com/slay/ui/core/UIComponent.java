package im.com.slay.ui.core;

import im.com.slay.ui.animation.AnimationTimeline;
import im.com.slay.ui.geometry.Insets;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.state.StateHandle;
import im.com.slay.ui.theme.ThemePalette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Root abstraction for all GUI elements. A component encapsulates state,
 * layout, styling and event handling in a single composable unit, similar to a
 * React component or a Vue single file component. It borrows concepts from
 * modern declarative frameworks while remaining compatible with the
 * constraints of Minecraft modding and the Labymod UI API.
 *
 * <p>The API is intentionally verbose to mirror the feature set of
 * browser-based frameworks. Components can register {@link StateHandle state
 * handles}, respond to layout passes, trigger animations and render themselves
 * onto arbitrary {@link SurfaceRenderer} implementations. By keeping the
 * contract stable and interface-driven it becomes trivial to port animations or
 * controls between desktop, in-game and even overlay renderers.</p>
 */
public abstract class UIComponent {

    private final List<UIComponent> children = new CopyOnWriteArrayList<UIComponent>();
    private UIComponent parent;
    private Vec2 preferredSize = Vec2.ZERO;
    private Vec2 actualSize = Vec2.ZERO;
    private Vec2 position = Vec2.ZERO;
    private Insets padding = Insets.NONE;
    private Insets margin = Insets.NONE;
    private String identifier;
    private boolean visible = true;
    private ThemePalette palette = ThemePalette.DEFAULT;
    private final List<AnimationTimeline> timelines = new ArrayList<AnimationTimeline>();

    /**
     * Adds a child component to the hierarchy. The parent reference is updated
     * automatically and layout will be invalidated to ensure the new element is
     * measured during the next pass.
     */
    public void addChild(UIComponent child) {
        if (child == null) {
            return;
        }
        children.add(child);
        child.parent = this;
        onChildAdded(child);
        invalidateLayout();
    }

    /**
     * Removes the specified child from the component tree. If the child is not
     * present the method is a no-op. This mirrors the behaviour of React's
     * reconciliation which gracefully handles removals without throwing.
     */
    public void removeChild(UIComponent child) {
        if (child == null) {
            return;
        }
        if (children.remove(child)) {
            child.parent = null;
            onChildRemoved(child);
            invalidateLayout();
        }
    }

    public List<UIComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public UIComponent getParent() {
        return parent;
    }

    public Vec2 getPreferredSize() {
        return preferredSize;
    }

    public Vec2 getActualSize() {
        return actualSize;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPreferredSize(Vec2 size) {
        this.preferredSize = size;
        invalidateLayout();
    }

    public void setPadding(Insets padding) {
        this.padding = padding;
        invalidateLayout();
    }

    public void setMargin(Insets margin) {
        this.margin = margin;
        invalidateLayout();
    }

    public Insets getPadding() {
        return padding;
    }

    public Insets getMargin() {
        return margin;
    }

    public UIComponent id(String id) {
        this.identifier = id;
        return this;
    }

    public String getId() {
        return identifier;
    }

    public ThemePalette getPalette() {
        return palette;
    }

    public void setPalette(ThemePalette palette) {
        if (palette == null) {
            return;
        }
        this.palette = palette;
        onThemeChanged(palette);
        for (UIComponent child : children) {
            child.setPalette(palette);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void addTimeline(AnimationTimeline timeline) {
        if (timeline != null) {
            timelines.add(timeline);
        }
    }

    public void removeTimeline(AnimationTimeline timeline) {
        timelines.remove(timeline);
    }

    public void update(double deltaTime) {
        for (AnimationTimeline timeline : timelines) {
            timeline.update(deltaTime);
        }
        for (UIComponent child : children) {
            child.update(deltaTime);
        }
    }

    public final void layout(Vec2 availableSize) {
        if (!visible) {
            return;
        }
        Vec2 measured = onMeasure(availableSize.subtract(new Vec2(margin.getHorizontal(), margin.getVertical())));
        this.actualSize = measured;
        onLayout();
        for (UIComponent child : children) {
            child.layout(measured);
        }
    }

    public final void render(UIContext context, SurfaceRenderer renderer) {
        if (!visible) {
            return;
        }
        Rect bounds = Rect.of(position.getX(), position.getY(), actualSize.getX(), actualSize.getY());
        Rect padded = bounds.inset(padding);
        renderer.saveState();
        renderer.translate(position.getX() + margin.getLeft(), position.getY() + margin.getTop(), 0);
        onRender(context, renderer, padded);
        for (UIComponent child : children) {
            child.render(context, renderer);
        }
        renderer.restoreState();
    }

    protected abstract Vec2 onMeasure(Vec2 availableSize);

    protected abstract void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds);

    protected void onLayout() {
        // Default layout does nothing. Subclasses can override to position children.
    }

    protected void onThemeChanged(ThemePalette palette) {
        // No-op by default. Components can react to theme transitions.
    }

    protected void onChildAdded(UIComponent child) {
        // Extension hook for containers.
    }

    protected void onChildRemoved(UIComponent child) {
        // Extension hook for containers.
    }

    public void attachToContext(UIContext context) {
        onAttach(context);
        for (UIComponent child : children) {
            child.attachToContext(context);
        }
    }

    public void detachFromContext(UIContext context) {
        onDetach(context);
        for (UIComponent child : children) {
            child.detachFromContext(context);
        }
    }

    protected void onAttach(UIContext context) {
    }

    protected void onDetach(UIContext context) {
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void bind(StateHandle<Vec2> stateHandle) {
        stateHandle.observe(new StateHandle.Observer<Vec2>() {
            @Override
            public void onChanged(Vec2 value) {
                setPosition(value);
            }
        });
    }

    protected void invalidateLayout() {
        UIComponent root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        root.requestRelayout();
    }

    protected void requestRelayout() {
        onRequestRelayout();
        for (UIComponent child : children) {
            child.requestRelayout();
        }
    }

    protected void onRequestRelayout() {
        // Hook for integration with external layout engines.
    }
}
