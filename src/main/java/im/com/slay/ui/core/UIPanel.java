package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.layout.FlexLayout;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.render.SurfaceRendererExtensions;

/**
 * Convenience container that draws a background panel with drop shadow like
 * modern overlay menus.
 */
public class UIPanel extends UIContainer {

    private int backgroundColor = 0xF0181818;
    private double cornerRadius = 12;
    private double elevation = 8;
    private double sheen = 0.35;

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

    public double getCornerRadius() {
        return cornerRadius;
    }

    public UIPanel elevation(double elevation) {
        this.elevation = Math.max(0, elevation);
        return this;
    }

    public double getElevation() {
        return elevation;
    }

    public UIPanel sheen(double sheen) {
        this.sheen = Math.max(0, Math.min(1, sheen));
        return this;
    }

    public double getSheen() {
        return sheen;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(Math.min(availableSize.getX(), 360), Math.min(availableSize.getY(), 240));
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        SurfaceRendererExtensions.drawElevatedPanel(renderer, bounds, backgroundColor, cornerRadius, elevation, sheen);
    }
}
