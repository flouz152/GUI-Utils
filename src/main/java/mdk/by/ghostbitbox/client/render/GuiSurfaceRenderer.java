package mdk.by.ghostbitbox.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.render.SurfaceRenderer;
import net.minecraft.client.gui.screen.Screen;

/**
 * Bridge between the UI surface renderer abstraction and the Minecraft GUI drawing helpers.
 */
public class GuiSurfaceRenderer implements SurfaceRenderer {

    private final Screen screen;
    private MatrixStack matrices;

    public GuiSurfaceRenderer(Screen screen) {
        this.screen = screen;
    }

    public void bind(MatrixStack matrices) {
        this.matrices = matrices;
    }

    @Override
    public void fillRect(Rect rect, int color) {
        if (matrices == null) {
            return;
        }
        int left = (int) Math.round(rect.getX());
        int top = (int) Math.round(rect.getY());
        int right = (int) Math.round(rect.getX() + rect.getWidth());
        int bottom = (int) Math.round(rect.getY() + rect.getHeight());
        screen.fill(matrices, left, top, right, bottom, color);
    }

    @Override
    public void drawString(String text, double x, double y, int color) {
        if (matrices == null) {
            return;
        }
        screen.drawString(matrices, text, (int) Math.round(x), (int) Math.round(y), color);
    }

    @Override
    public void drawCenteredString(String text, double centerX, double y, int color) {
        if (matrices == null) {
            return;
        }
        screen.drawCenteredString(matrices, text, (int) Math.round(centerX), (int) Math.round(y), color);
    }
}
