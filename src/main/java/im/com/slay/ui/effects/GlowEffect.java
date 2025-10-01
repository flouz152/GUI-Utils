package im.com.slay.ui.effects;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Simulates a soft neon glow that wraps around a target rectangle. The glow is
 * rendered using multiple expanding passes which keeps it compatible with the
 * immediate mode renderer Labymod exposes while still approximating a Gaussian
 * blur. Intensities and color can be tweaked to match different palette tones.
 */
public final class GlowEffect {

    private int color = 0xFF2E7BFF;
    private double spread = 48.0;
    private double intensity = 0.65;
    private int layers = 6;

    public GlowEffect color(int color) {
        this.color = color;
        return this;
    }

    public GlowEffect spread(double spread) {
        this.spread = Math.max(4.0, spread);
        return this;
    }

    public GlowEffect intensity(double intensity) {
        this.intensity = Math.max(0.0, intensity);
        return this;
    }

    public GlowEffect layers(int layers) {
        this.layers = Math.max(1, layers);
        return this;
    }

    public void render(SurfaceRenderer renderer, Rect target, double cornerRadius) {
        int passCount = Math.max(1, layers);
        double expansionBase = Math.max(1.0, spread);
        for (int i = passCount - 1; i >= 0; i--) {
            double layerProgress = (i + 1) / (double) passCount;
            double expansion = expansionBase * layerProgress;
            double alpha = intensity * Math.pow(layerProgress, 1.35);
            int tinted = ColorUtil.withOpacity(color, Math.min(0.95, alpha));
            renderer.drawRect(target.expand(expansion), tinted, cornerRadius + expansion);
        }
    }
}
