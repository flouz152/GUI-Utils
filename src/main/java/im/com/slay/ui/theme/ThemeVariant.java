package im.com.slay.ui.theme;

import im.com.slay.ui.theme.ThemePalette.ColorMutation;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Helper for generating light/dark variants of a palette.
 */
public final class ThemeVariant {

    private ThemeVariant() {
    }

    public static ThemePalette lighten(ThemePalette palette, double amount) {
        return palette.mutate("background", new ColorMutation() {
            @Override
            public int apply(int input) {
                return ColorUtil.lighten(input, amount);
            }
        }).mutate("surface", new ColorMutation() {
            @Override
            public int apply(int input) {
                return ColorUtil.lighten(input, amount * 0.8);
            }
        });
    }

    public static ThemePalette darken(ThemePalette palette, double amount) {
        return palette.mutate("background", new ColorMutation() {
            @Override
            public int apply(int input) {
                return ColorUtil.darken(input, amount);
            }
        }).mutate("surface", new ColorMutation() {
            @Override
            public int apply(int input) {
                return ColorUtil.darken(input, amount * 0.8);
            }
        });
    }
}
