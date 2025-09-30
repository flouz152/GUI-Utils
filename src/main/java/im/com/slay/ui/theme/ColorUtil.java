package im.com.slay.ui.theme;

/**
 * Helper for color manipulation used when composing gradients and shadows with
 * basic renderer primitives. Using integer math keeps compatibility with the
 * Labymod runtime while still enabling modern neumorphic styles.
 */
public final class ColorUtil {

    private ColorUtil() {
    }

    public static int withOpacity(int color, double opacity) {
        int clamped = (int) Math.round(Math.max(0.0, Math.min(1.0, opacity)) * 255.0);
        return (clamped << 24) | (color & 0x00FFFFFF);
    }

    public static int lighten(int color, double amount) {
        double clamp = Math.max(0.0, Math.min(1.0, amount));
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        r = (int) Math.round(r + (255 - r) * clamp);
        g = (int) Math.round(g + (255 - g) * clamp);
        b = (int) Math.round(b + (255 - b) * clamp);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int darken(int color, double amount) {
        double clamp = Math.max(0.0, Math.min(1.0, amount));
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        r = (int) Math.round(r * (1.0 - clamp));
        g = (int) Math.round(g * (1.0 - clamp));
        b = (int) Math.round(b * (1.0 - clamp));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int blend(int foreground, int background, double ratio) {
        double clamp = Math.max(0.0, Math.min(1.0, ratio));
        double inv = 1.0 - clamp;
        int fa = (foreground >> 24) & 0xFF;
        int fr = (foreground >> 16) & 0xFF;
        int fg = (foreground >> 8) & 0xFF;
        int fb = foreground & 0xFF;
        int ba = (background >> 24) & 0xFF;
        int br = (background >> 16) & 0xFF;
        int bg = (background >> 8) & 0xFF;
        int bb = background & 0xFF;
        int a = (int) Math.round(fa * clamp + ba * inv);
        int r = (int) Math.round(fr * clamp + br * inv);
        int g = (int) Math.round(fg * clamp + bg * inv);
        int b = (int) Math.round(fb * clamp + bb * inv);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
