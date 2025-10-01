package im.com.slay.ui.theme;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry of named themes with listeners.
 */
public final class ThemeManager {

    public interface Listener {
        void onThemeChanged(String name, ThemePalette palette);
    }

    private final List<ThemeEntry> entries = new ArrayList<ThemeEntry>();
    private final List<Listener> listeners = new ArrayList<Listener>();
    private ThemePalette active = ThemePalette.DEFAULT;
    private String activeName = "default";

    public ThemeManager register(String name, ThemePalette palette) {
        entries.add(new ThemeEntry(name, palette));
        return this;
    }

    public ThemeManager unregister(String name) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).name.equals(name)) {
                entries.remove(i);
                break;
            }
        }
        return this;
    }

    public ThemePalette get(String name) {
        for (int i = 0; i < entries.size(); i++) {
            ThemeEntry entry = entries.get(i);
            if (entry.name.equals(name)) {
                return entry.palette;
            }
        }
        return ThemePalette.DEFAULT;
    }

    public void apply(String name) {
        active = get(name);
        activeName = name;
        notifyListeners();
    }

    public ThemePalette active() {
        return active;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
        listener.onThemeChanged(activeName, active);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onThemeChanged(activeName, active);
        }
    }

    private static final class ThemeEntry {
        private final String name;
        private final ThemePalette palette;

        private ThemeEntry(String name, ThemePalette palette) {
            this.name = name;
            this.palette = palette;
        }
    }
}
