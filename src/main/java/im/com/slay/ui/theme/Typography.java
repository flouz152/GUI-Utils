package im.com.slay.ui.theme;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores font families and weights. Exposes helpers to scale fonts based on
 * viewport size which allows dynamic UI scaling like modern web apps.
 */
public final class Typography {

    private final Map<String, Font> fonts = new HashMap<String, Font>();

    public Typography register(String token, String family, double size, double weight) {
        fonts.put(token, new Font(family, size, weight));
        return this;
    }

    public Font get(String token) {
        Font font = fonts.get(token);
        if (font == null) {
            return new Font("Minecraftia", 14, 400);
        }
        return font;
    }

    public static final class Font {
        public final String family;
        public final double size;
        public final double weight;

        public Font(String family, double size, double weight) {
            this.family = family;
            this.size = size;
            this.weight = weight;
        }
    }
}
