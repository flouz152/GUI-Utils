package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import java.util.function.Consumer;
import org.lwjgl.input.Keyboard;

/**
 * Simple text input component used for the search field.
 */
public class UITextField extends UIComponent {

    private final String placeholder;
    private String text = "";
    private boolean focused;
    private int caretPosition;
    private Consumer<String> onChanged;

    public UITextField(String placeholder) {
        this.placeholder = placeholder;
        setPreferredSize(new Vec2(220, 20));
    }

    public void setOnChanged(Consumer<String> onChanged) {
        this.onChanged = onChanged;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
        caretPosition = Math.min(this.text.length(), caretPosition);
        notifyChanged();
    }

    public void handleKeyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_BACK) {
            backspace();
            return;
        }

        if (keyCode == Keyboard.KEY_RETURN) {
            notifyChanged();
            return;
        }

        if (!Character.isISOControl(typedChar)) {
            insertChar(typedChar);
        }
    }

    private void insertChar(char typedChar) {
        String prefix = text.substring(0, caretPosition);
        String suffix = text.substring(caretPosition);
        text = prefix + typedChar + suffix;
        caretPosition++;
        notifyChanged();
    }

    private void backspace() {
        if (caretPosition <= 0 || text.isEmpty()) {
            return;
        }
        String prefix = text.substring(0, caretPosition - 1);
        String suffix = text.substring(caretPosition);
        text = prefix + suffix;
        caretPosition--;
        notifyChanged();
    }

    private void notifyChanged() {
        if (onChanged != null) {
            onChanged.accept(text);
        }
    }

    public void moveCaretToEnd() {
        caretPosition = text.length();
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        double width = Math.min(getPreferredSize().getX(), availableSize.getX());
        return new Vec2(width, getPreferredSize().getY());
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        int background = focused ? 0xAA2A2A2A : 0x88202020;
        renderer.fillRect(rect, background);
        Rect inner = rect.inset(4.0, 3.0);

        String toDisplay = text.isEmpty() ? placeholder : text;
        int color = text.isEmpty() ? 0x77FFFFFF : 0xFFFFFFFF;
        renderer.drawString(toDisplay, inner.getX(), inner.getY() + inner.getHeight() / 2.0 - 4.0, color);

        if (focused && ((context.getFrameTimeMs() / 500L) % 2L == 0L)) {
            double caretX = inner.getX() + Math.min(inner.getWidth() - 2.0, caretPosition * 6.0);
            Rect caret = new Rect(caretX, inner.getY(), 1.0, inner.getHeight());
            renderer.fillRect(caret, 0xFFFFFFFF);
        }
    }
}
