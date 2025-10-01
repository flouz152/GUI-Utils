package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.util.Easings;

/**
 * Toggle button that interpolates between on/off states using an easing function.
 */
public class UIToggle extends UIButton {

    private boolean checked;
    private double animationProgress;

    public UIToggle(String text) {
        super(text);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        double target = checked ? 1.0 : 0.0;
        double delta = target - animationProgress;
        animationProgress += delta * 0.3;
        double eased = Easings.easeOutBack(animationProgress);
        renderer.fillRect(rect, (int) (0x4400FF00 * eased));
        super.onRender(context, renderer, rect);
    }
}
