package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Base class for all UI components. Subclasses override {@link #onMeasure(Vec2)} and
 * {@link #onRender(UIContext, SurfaceRenderer, Rect)} to provide behaviour.
 */
public abstract class UIComponent {

    private Vec2 preferredSize = Vec2.ZERO;
    private Rect lastRenderRect = Rect.EMPTY;

    public Vec2 getPreferredSize() {
        return preferredSize;
    }

    public void setPreferredSize(Vec2 preferredSize) {
        this.preferredSize = preferredSize;
    }

    public final Vec2 measure(Vec2 availableSize) {
        Vec2 measured = onMeasure(availableSize);
        preferredSize = measured;
        return measured;
    }

    protected Vec2 onMeasure(Vec2 availableSize) {
        return preferredSize != null ? preferredSize : Vec2.ZERO;
    }

    public final void render(UIContext context, SurfaceRenderer renderer, Rect rect) {
        lastRenderRect = rect;
        onRender(context, renderer, rect);
    }

    public Rect getLastRenderRect() {
        return lastRenderRect;
    }

    protected abstract void onRender(UIContext context, SurfaceRenderer renderer, Rect rect);
}
