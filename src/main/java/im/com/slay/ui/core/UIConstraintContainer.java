package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.layout.ConstraintLayout;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Container that uses {@link ConstraintLayout} for positioning children.
 */
public final class UIConstraintContainer extends UIContainer {

    public UIConstraintContainer() {
        super(new ConstraintLayout());
    }

    @Override
    public void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        if (getLayout() != null) {
            getLayout().layout(this, rect, context, renderer);
        }
    }
}
