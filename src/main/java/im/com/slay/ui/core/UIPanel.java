package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.layout.Layout;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Container with a padded background used to represent the window surface.
 */
public class UIPanel extends UIContainer {

    private double padding = 8.0;
    private int backgroundColor = 0xCC1C1C1C;
    private int borderColor = 0x55FFFFFF;

    public UIPanel() {
    }

    public UIPanel(Layout layout) {
        super(layout);
    }

    public void setPadding(double padding) {
        this.padding = padding;
    }

    public double getPadding() {
        return padding;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        Vec2 innerAvailable = new Vec2(
                Math.max(0.0, availableSize.getX() - padding * 2.0),
                Math.max(0.0, availableSize.getY() - padding * 2.0));
        Vec2 innerSize = super.onMeasure(innerAvailable);
        return new Vec2(innerSize.getX() + padding * 2.0, innerSize.getY() + padding * 2.0);
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        renderer.fillRect(rect, backgroundColor);

        // Draw a simple border outline.
        renderer.fillRect(new Rect(rect.getX(), rect.getY(), rect.getWidth(), 1.0), borderColor);
        renderer.fillRect(new Rect(rect.getX(), rect.getY() + rect.getHeight() - 1.0, rect.getWidth(), 1.0), borderColor);
        renderer.fillRect(new Rect(rect.getX(), rect.getY(), 1.0, rect.getHeight()), borderColor);
        renderer.fillRect(new Rect(rect.getX() + rect.getWidth() - 1.0, rect.getY(), 1.0, rect.getHeight()), borderColor);

        Rect inner = rect.inset(padding);
        if (getLayout() != null) {
            getLayout().layout(this, inner, context, renderer);
        }
    }
}
