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
    private double glassiness = 0.0;
    private int accentColor = 0xFF00A3FF;
    private double glowIntensity = 0.0;
    private double glowSpread = 42.0;
    private int glowColor = 0xFF00A3FF;

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

    public UIPanel glass(double glassiness) {
        this.glassiness = Math.max(0.0, Math.min(1.0, glassiness));
        return this;
    }

    public double getGlassiness() {
        return glassiness;
    }

    public UIPanel accentColor(int accentColor) {
        this.accentColor = accentColor;
        return this;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public UIPanel glow(int color, double intensity) {
        return glow(color, intensity, glowSpread);
    }

    public UIPanel glow(int color, double intensity, double spread) {
        this.glowColor = color;
        this.glowIntensity = Math.max(0.0, intensity);
        this.glowSpread = Math.max(8.0, spread);
        return this;
    }

    public double getGlowIntensity() {
        return glowIntensity;
    }

    public double getGlowSpread() {
        return glowSpread;
    }

    public int getGlowColor() {
        return glowColor;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(Math.min(availableSize.getX(), 360), Math.min(availableSize.getY(), 240));
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        if (glowIntensity > 0.0) {
            SurfaceRendererExtensions.drawGlow(renderer, bounds, glowColor, cornerRadius, glowIntensity, glowSpread);
        }
        if (glassiness > 0.0) {
            SurfaceRendererExtensions.drawGlassPanel(renderer, bounds, backgroundColor, accentColor, cornerRadius, elevation, sheen, glassiness);
        } else {
            SurfaceRendererExtensions.drawElevatedPanel(renderer, bounds, backgroundColor, cornerRadius, elevation, sheen);
        }
    }
}
