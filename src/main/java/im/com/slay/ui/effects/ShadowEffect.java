package im.com.slay.ui.effects;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Simple drop shadow generator that renders a layered shadow beneath a rect.
 */
public final class ShadowEffect {

    private double radius = 18;
    private double offsetX = 0;
    private double offsetY = 6;
    private double opacity = 0.3;
    private int color = 0xFF000000;

    public ShadowEffect radius(double radius) {
        this.radius = radius;
        return this;
    }

    public ShadowEffect offset(double x, double y) {
        this.offsetX = x;
        this.offsetY = y;
        return this;
    }

    public ShadowEffect color(int color) {
        this.color = color;
        return this;
    }

    public ShadowEffect opacity(double opacity) {
        this.opacity = opacity;
        return this;
    }

    public void render(SurfaceRenderer renderer, Rect target) {
        double steps = Math.max(1, radius / 4.0);
        for (int i = 0; i < steps; i++) {
            double progress = (i + 1) / steps;
            double blurRadius = radius * progress;
            int blended = ColorUtil.withOpacity(color, opacity * (1.0 - progress * 0.7));
            Rect expanded = target.expand(blurRadius).offset(offsetX * progress, offsetY * progress);
            renderer.drawRect(expanded, blended, expanded.getHeight() / 6.0);
        }
    }
}
