package net.minecraft.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

/**
 * Minimal stub of the Minecraft client used for compilation.
 */
public class Minecraft {

    private static final Minecraft INSTANCE = new Minecraft();

    private final FontRenderer fontRenderer = new FontRenderer();
    private Screen currentScreen;

    public static Minecraft getInstance() {
        return INSTANCE;
    }

    /**
     * Legacy accessor retained for backwards compatibility with existing code paths.
     */
    public static Minecraft getMinecraft() {
        return getInstance();
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.onClose();
            currentScreen.removed();
        }
        currentScreen = screen;
        if (currentScreen != null) {
            currentScreen.init(this, 854, 480);
        }
    }

    public void displayGuiScreen(Screen screen) {
        setScreen(screen);
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }
}
