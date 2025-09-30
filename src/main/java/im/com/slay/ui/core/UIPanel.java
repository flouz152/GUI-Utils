package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.layout.FlexLayout;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Convenience container that draws a background panel with drop shadow like
 * modern overlay menus.
 */
public final class UIPanel extends UIContainer {

    private int backgroundColor = 0xF0181818;
    private double cornerRadius = 12;

    public UIPanel() {
        super(FlexLayout.vertical());
        setPadding(new im.com.slay.ui.geometry.Insets(16, 16, 16, 16));
        setMargin(new im.com.slay.ui.geometry.Insets(8, 8, 8, 8));
    }

    public UIPanel backgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public UIPanel cornerRadius(double cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(Math.min(availableSize.getX(), 360), Math.min(availableSize.getY(), 240));
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        renderer.drawRect(bounds, backgroundColor, cornerRadius);
    }
}
