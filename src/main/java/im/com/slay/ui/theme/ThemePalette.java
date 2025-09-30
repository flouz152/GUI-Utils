package im.com.slay.ui.theme;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a color palette with semantic slots. Allows quick theming
 * transitions inspired by design systems like Material You.
 */
public final class ThemePalette {

    public static final ThemePalette DEFAULT = new ThemePalette()
            .color("background", 0xC8101010)
            .color("surface", 0xF0181818)
            .color("primary", 0xFF00A3FF)
            .color("primaryVariant", 0xFF0064FF)
            .color("onPrimary", 0xFFFFFFFF)
            .color("accent", 0xFFFF6F91)
            .color("success", 0xFF64FFDA)
            .color("warning", 0xFFFFD166)
            .color("danger", 0xFFFF6B6B)
            .color("textPrimary", 0xFFFFFFFF)
            .color("textSecondary", 0xB3FFFFFF);

    private final Map<String, Integer> colors = new HashMap<String, Integer>();

    public ThemePalette color(String slot, int color) {
        colors.put(slot, color);
        return this;
    }

    public int color(String slot) {
        Integer value = colors.get(slot);
        if (value == null) {
            return 0xFFFFFFFF;
        }
        return value;
    }

    public ThemePalette merge(ThemePalette other) {
        ThemePalette result = new ThemePalette();
        result.colors.putAll(colors);
        result.colors.putAll(other.colors);
        return result;
    }
}
