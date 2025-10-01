package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.layout.FlexLayout;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.util.Easings;

/**
 * Simple modal dialog container that fades in using an easing function.
 */
public class UIModal extends UIContainer {

    private double animationProgress;

    public UIModal() {
        super(FlexLayout.vertical());
    }

    public void setVisible(boolean visible) {
        animationProgress = visible ? 1.0 : 0.0;
    }

    @Override
    public void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        animationProgress = Math.min(1.0, Math.max(0.0, animationProgress));
        double eased = Easings.easeOutBack(animationProgress);
        renderer.fillRect(rect, (int) (0x66000000 * eased));
        if (getLayout() != null) {
            getLayout().layout(this, rect, context, renderer);
        }
    }
}
