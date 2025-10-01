package im.com.slay.ui.effects;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Frosted glass blur simulation using multiple translucent passes.
 */
public final class BlurEffect {

    private int tintColor = 0x80FFFFFF;
    private double intensity = 0.5;
    private int passes = 4;

    public BlurEffect tint(int color) {
        this.tintColor = color;
        return this;
    }

    public BlurEffect intensity(double intensity) {
        this.intensity = intensity;
        return this;
    }

    public BlurEffect passes(int passes) {
        this.passes = passes;
        return this;
    }

    public void render(SurfaceRenderer renderer, Rect bounds) {
        for (int i = 0; i < passes; i++) {
            double progress = (i + 1) / (double) passes;
            int color = ColorUtil.withOpacity(tintColor, intensity * (1.0 - progress * 0.5));
            double expansion = progress * 6;
            renderer.drawRect(bounds.expand(expansion), color, bounds.getHeight() / 3.0);
        }
    }
}
