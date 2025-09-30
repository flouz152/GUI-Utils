package im.com.slay.ui.render;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Collection of high level drawing helpers that build on top of the primitive
 * {@link SurfaceRenderer} contract. These routines emulate layered drop shadows
 * and glossy highlights purely with rectangle draws which keeps them compatible
 * with the Labymod pipeline while still producing modern rounded surfaces.
 */
public final class SurfaceRendererExtensions {

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
}
