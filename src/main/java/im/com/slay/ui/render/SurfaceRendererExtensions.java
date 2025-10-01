package im.com.slay.ui.render;

import im.com.slay.ui.effects.GlowEffect;
import im.com.slay.ui.geometry.Insets;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Collection of high level drawing helpers that build on top of the primitive
 * {@link SurfaceRenderer} contract. These routines emulate layered drop shadows
 * and glossy highlights purely with rectangle draws which keeps them compatible
 * with the Labymod pipeline while still producing modern rounded surfaces.
 */
public final class SurfaceRendererExtensions {

    private static final GlowEffect DEFAULT_GLOW = new GlowEffect();

    private SurfaceRendererExtensions() {
    }

    public static void drawElevatedPanel(SurfaceRenderer renderer, Rect bounds, int baseColor,
                                         double cornerRadius, double elevation, double sheen) {
        double shadowOffset = Math.max(1.0, elevation);
        double blurExpansion = Math.min(12.0, elevation * 1.5);
        Rect shadowRect = bounds.offset(0, shadowOffset).expand(blurExpansion);
        int shadowColor = ColorUtil.withOpacity(ColorUtil.darken(baseColor, 0.65), 0.35 + Math.min(0.25, elevation / 24.0));
        renderer.drawRect(shadowRect, shadowColor, cornerRadius + blurExpansion);

        Rect highlightRect = bounds.offset(0, -Math.max(0.5, elevation / 2.0));
        int highlightColor = ColorUtil.withOpacity(ColorUtil.lighten(baseColor, 0.4), Math.min(0.45, sheen));
        renderer.drawRect(highlightRect, highlightColor, Math.max(0.0, cornerRadius - 2.0));

        renderer.drawRect(bounds, baseColor, cornerRadius);
    }

    public static void drawGlassPanel(SurfaceRenderer renderer, Rect bounds, int baseColor, int accentColor,
                                      double cornerRadius, double elevation, double sheen, double glassiness) {
        drawElevatedPanel(renderer, bounds, baseColor, cornerRadius, elevation, sheen);
        double intensity = Math.max(0.0, Math.min(1.0, glassiness));
        Rect inner = bounds.inset(new Insets(3, 3, 3, 3));
        GradientBrush vertical = new GradientBrush()
                .addStop(0.0, ColorUtil.withOpacity(ColorUtil.lighten(baseColor, 0.4), 0.35 * intensity))
                .addStop(0.45, ColorUtil.withOpacity(ColorUtil.blend(accentColor, baseColor, 0.65), 0.28 * intensity + sheen * 0.1))
                .addStop(1.0, ColorUtil.withOpacity(ColorUtil.darken(baseColor, 0.2), 0.32 * intensity));
        vertical.fill(renderer, inner, GradientBrush.Orientation.VERTICAL, Math.max(0.0, cornerRadius - 3.0), 28);

        double ribbonHeight = Math.max(6.0, inner.getHeight() * 0.18);
        Rect ribbon = Rect.of(inner.getLeft(), inner.getTop(), inner.getWidth(), ribbonHeight);
        int ribbonColor = ColorUtil.withOpacity(ColorUtil.lighten(accentColor, 0.55), 0.42 * intensity + sheen * 0.3);
        renderer.drawRect(ribbon, ribbonColor, Math.max(0.0, cornerRadius - 4.0));

        Rect bottomShine = Rect.of(inner.getLeft(), inner.getBottom() - ribbonHeight * 0.6, inner.getWidth(), ribbonHeight * 0.6);
        int bottomColor = ColorUtil.withOpacity(ColorUtil.darken(accentColor, 0.25), 0.25 * intensity);
        renderer.drawRect(bottomShine, bottomColor, Math.max(0.0, cornerRadius - 4.0));
    }

    public static void drawGradientFill(SurfaceRenderer renderer, Rect bounds, GradientBrush brush,
                                        GradientBrush.Orientation orientation, double cornerRadius) {
        brush.fill(renderer, bounds, orientation, cornerRadius, 36);
    }

    public static void drawGradientStroke(SurfaceRenderer renderer, Rect bounds, GradientBrush brush,
                                          double thickness, double cornerRadius) {
        int steps = Math.max(1, (int) Math.round(thickness * 1.5));
        for (int i = 0; i < steps; i++) {
            double progress = i / (double) Math.max(1, steps - 1);
            int color = brush.sample(progress);
            double insetAmount = (i / (double) steps) * thickness;
            Rect ring = bounds.inset(new Insets(insetAmount, insetAmount, insetAmount, insetAmount));
            renderer.drawRect(ring, ColorUtil.withOpacity(color, 0.85 - progress * 0.5), Math.max(0.0, cornerRadius - insetAmount));
        }
    }

    public static void drawGlow(SurfaceRenderer renderer, Rect bounds, int color, double cornerRadius,
                                double intensity, double spread) {
        DEFAULT_GLOW.color(color).intensity(intensity).spread(spread).render(renderer, bounds, cornerRadius);
    }

    public static void drawAurora(SurfaceRenderer renderer, Rect bounds, GradientBrush palette,
                                  double sway, double cornerRadius) {
        int stripes = 18;
        for (int i = 0; i < stripes; i++) {
            double progress = i / (double) stripes;
            double offset = Math.sin((progress + sway) * Math.PI * 2.0) * bounds.getHeight() * 0.12;
            double sliceWidth = bounds.getWidth() / stripes * 1.2;
            Rect slice = Rect.of(bounds.getLeft() + progress * bounds.getWidth() - sliceWidth * 0.25,
                    bounds.getTop() + offset, sliceWidth, bounds.getHeight());
            int color = ColorUtil.withOpacity(palette.sample((progress + sway) % 1.0), 0.16);
            renderer.drawRect(slice, color, cornerRadius);
        }
    }
}
