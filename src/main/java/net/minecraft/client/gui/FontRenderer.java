package net.minecraft.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

/**
 * Minimal font renderer stub that provides width calculation and draw helpers.
 */
public class FontRenderer {

    public int draw(MatrixStack matrices, String text, int x, int y, int color) {
        // Rendering is not simulated inside the stub, the return value mirrors the vanilla API
        // and represents the x position immediately after the drawn string.
        return x + width(text);
    }

    public int width(String text) {
        return text != null ? text.length() * 6 : 0;
    }
}
