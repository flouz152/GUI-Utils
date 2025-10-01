package mdk.by.ghostbitbox.client.render;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.render.SurfaceRenderer;
import net.minecraft.client.gui.GuiScreen;

/**
 * Bridge between the UI surface renderer abstraction and the Minecraft GUI drawing helpers.
 */
public class GuiSurfaceRenderer implements SurfaceRenderer {

    private final GuiScreen screen;

    public GuiSurfaceRenderer(GuiScreen screen) {
        this.screen = screen;
    }

    @Override
    public void fillRect(Rect rect, int color) {
        int left = (int) Math.round(rect.getX());
        int top = (int) Math.round(rect.getY());
        int right = (int) Math.round(rect.getX() + rect.getWidth());
        int bottom = (int) Math.round(rect.getY() + rect.getHeight());
        screen.drawRect(left, top, right, bottom, color);
    }

    @Override
    public void drawString(String text, double x, double y, int color) {
        screen.drawString(text, (int) Math.round(x), (int) Math.round(y), color);
    }

    @Override
    public void drawCenteredString(String text, double centerX, double y, int color) {
        screen.drawCenteredString(text, (int) Math.round(centerX), (int) Math.round(y), color);
    }
}
