package net.minecraft.client;

import net.minecraft.client.gui.GuiScreen;

/**
 * Minimal stub of the Minecraft client used for compilation.
 */
public class Minecraft {

    private static final Minecraft INSTANCE = new Minecraft();

    private GuiScreen currentScreen;

    public static Minecraft getMinecraft() {
        return INSTANCE;
    }

    public void displayGuiScreen(GuiScreen screen) {
        if (currentScreen != null) {
            currentScreen.onGuiClosed();
        }
        currentScreen = screen;
        if (currentScreen != null) {
            currentScreen.initGui();
        }
    }

    public GuiScreen getCurrentScreen() {
        return currentScreen;
    }
}
