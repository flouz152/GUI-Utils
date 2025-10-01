package net.minecraft.client.gui;

/**
 * Minimal stub of the Minecraft GuiScreen class for compilation.
 */
public abstract class GuiScreen {

    protected int width = 320;
    protected int height = 240;

    public void initGui() {
    }

    public void onGuiClosed() {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    protected void keyTyped(char typedChar, int keyCode) {
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawRect(int left, int top, int right, int bottom, int color) {
    }

    public void drawString(String text, int x, int y, int color) {
    }

    public void drawCenteredString(String text, int x, int y, int color) {
    }
}
