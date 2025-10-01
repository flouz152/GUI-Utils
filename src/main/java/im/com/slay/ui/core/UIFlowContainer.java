package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.layout.FlowLayout;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Container that arranges children vertically using {@link FlowLayout}.
 */
public final class UIFlowContainer extends UIContainer {

    public UIFlowContainer() {
        super(new FlowLayout());
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        if (getLayout() != null) {
            getLayout().layout(this, rect, context, renderer);
        }
    }
}
