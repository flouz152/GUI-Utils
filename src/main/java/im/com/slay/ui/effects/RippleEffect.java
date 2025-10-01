package im.com.slay.ui.effects;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Animated ripple used for hover or idle background motion. The effect renders
 * concentric rings that expand outward; progress is expected to be provided by
 * a looping animation timeline.
 */
public final class RippleEffect {

    private int color = 0xFF4DA8FF;
    private double intensity = 0.35;
    private double radiusMultiplier = 0.8;
    private int rings = 3;
    private double thickness = 18.0;

    public RippleEffect color(int color) {
        this.color = color;
        return this;
    }

    public RippleEffect intensity(double intensity) {
        this.intensity = Math.max(0.0, intensity);
        return this;
    }

    public RippleEffect radiusMultiplier(double radiusMultiplier) {
        this.radiusMultiplier = Math.max(0.1, radiusMultiplier);
        return this;
    }

    public RippleEffect rings(int rings) {
        this.rings = Math.max(1, rings);
        return this;
    }

    public RippleEffect thickness(double thickness) {
        this.thickness = Math.max(1.0, thickness);
        return this;
    }

    public void render(SurfaceRenderer renderer, Rect bounds, double progress) {
        int ringCount = Math.max(1, rings);
        double maxDimension = Math.max(bounds.getWidth(), bounds.getHeight());
        double maxRadius = maxDimension * radiusMultiplier;
        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();
        for (int i = 0; i < ringCount; i++) {
            double offset = i / (double) ringCount;
            double ringProgress = progress + offset;
            while (ringProgress > 1.0) {
                ringProgress -= 1.0;
            }
            double radius = maxRadius * ringProgress;
            double opacity = intensity * Math.max(0.0, 1.0 - ringProgress);
            double localThickness = Math.min(thickness, radius * 0.5 + 1.0);
            Rect circle = Rect.of(centerX - radius, centerY - radius, radius * 2.0, radius * 2.0);
            int tinted = ColorUtil.withOpacity(color, opacity);
            renderer.drawCircle(circle.expand(localThickness * 0.5), tinted, 1.0);
        }
    }
}
