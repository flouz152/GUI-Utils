package net.minecraft.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

/**
 * Lightweight approximation of Minecraft's {@code Screen} class that is sufficient for compiling
 * addon sources inside the kata environment.
 */
public abstract class Screen {

    protected final ITextComponent title;
    protected Minecraft minecraft;
    protected FontRenderer font;
    protected int width = 854;
    protected int height = 480;

    protected Screen(ITextComponent title) {
        this.title = title;
    }

    public void init(Minecraft minecraft, int width, int height) {
        this.minecraft = minecraft;
        this.font = minecraft.getFontRenderer();
        this.width = width;
        this.height = height;
        init();
    }

    protected void init() {
    }

    public void tick() {
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    public void resize(Minecraft minecraft, int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void removed() {
    }

    public void onClose() {
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void renderBackground(MatrixStack matrices) {
    }

    public void fill(MatrixStack matrices, int left, int top, int right, int bottom, int color) {
    }

    public void drawString(MatrixStack matrices, String text, int x, int y, int color) {
        if (font != null) {
            font.draw(matrices, text, x, y, color);
        }
    }

    public void drawCenteredString(MatrixStack matrices, String text, int x, int y, int color) {
        if (font != null) {
            int textWidth = font.width(text);
            font.draw(matrices, text, x - textWidth / 2, y, color);
        }
    }
}
