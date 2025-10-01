package im.com.slay.ui.theme;

import im.com.slay.ui.animation.Easings;

/**
 * Helper to animate between palettes. Uses simple interpolation on each color
 * channel and exposes a hook to mix typography scales.
 */
public final class ThemeTransition {

    private ThemePalette start = ThemePalette.DEFAULT;
    private ThemePalette end = ThemePalette.DEFAULT;
    private double progress;

    public ThemeTransition from(ThemePalette palette) {
        this.start = palette;
        return this;
    }

    public ThemeTransition to(ThemePalette palette) {
        this.end = palette;
        return this;
    }

    public ThemePalette evaluate(double t) {
        progress = Easings.easeInOutCubic(t);
        ThemePalette palette = new ThemePalette();
        palette.color("background", mix(start.color("background"), end.color("background"), progress));
        palette.color("surface", mix(start.color("surface"), end.color("surface"), progress));
        palette.color("primary", mix(start.color("primary"), end.color("primary"), progress));
        palette.color("accent", mix(start.color("accent"), end.color("accent"), progress));
        palette.color("textPrimary", mix(start.color("textPrimary"), end.color("textPrimary"), progress));
        palette.color("textSecondary", mix(start.color("textSecondary"), end.color("textSecondary"), progress));
        return palette;
    }

    private int mix(int start, int end, double t) {
        int sa = (start >> 24) & 0xFF;
        int sr = (start >> 16) & 0xFF;
        int sg = (start >> 8) & 0xFF;
        int sb = start & 0xFF;

        int ea = (end >> 24) & 0xFF;
        int er = (end >> 16) & 0xFF;
        int eg = (end >> 8) & 0xFF;
        int eb = end & 0xFF;

        int a = (int) (sa + (ea - sa) * t);
        int r = (int) (sr + (er - sr) * t);
        int g = (int) (sg + (eg - sg) * t);
        int b = (int) (sb + (eb - sb) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
