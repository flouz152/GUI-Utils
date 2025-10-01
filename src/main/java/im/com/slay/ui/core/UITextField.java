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

    private static final int GLFW_KEY_BACKSPACE = 259;
    private static final int GLFW_KEY_ENTER = 257;
    private static final int GLFW_KEY_DELETE = 261;
    private static final int GLFW_KEY_LEFT = 263;
    private static final int GLFW_KEY_RIGHT = 262;
    private static final int GLFW_KEY_HOME = 268;
    private static final int GLFW_KEY_END = 269;

    private final String placeholder;
    private String text = "";
    private boolean focused;
    private int caretPosition;
    private Consumer<String> onChanged;

    public UITextField(String placeholder) {
        this.placeholder = placeholder;
        setPreferredSize(new Vec2(240, 20));
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

    public boolean handleKeyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case Keyboard.KEY_BACK:
            case GLFW_KEY_BACKSPACE:
                backspace();
                return true;
            case Keyboard.KEY_RETURN:
            case GLFW_KEY_ENTER:
                notifyChanged();
                return true;
            case Keyboard.KEY_DELETE:
            case GLFW_KEY_DELETE:
                delete();
                return true;
            case Keyboard.KEY_LEFT:
            case GLFW_KEY_LEFT:
                moveCaret(-1);
                return true;
            case Keyboard.KEY_RIGHT:
            case GLFW_KEY_RIGHT:
                moveCaret(1);
                return true;
            case Keyboard.KEY_HOME:
            case GLFW_KEY_HOME:
                caretPosition = 0;
                return true;
            case Keyboard.KEY_END:
            case GLFW_KEY_END:
                caretPosition = text.length();
                return true;
            default:
                return false;
        }
    }

    public boolean handleCharTyped(char typedChar, int modifiers) {
        if (Character.isISOControl(typedChar)) {
            return false;
        }
        insertChar(typedChar);
        return true;
    }

    private void moveCaret(int delta) {
        caretPosition = Math.max(0, Math.min(text.length(), caretPosition + delta));
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

    private void delete() {
        if (caretPosition >= text.length()) {
            return;
        }
        String prefix = text.substring(0, caretPosition);
        String suffix = text.substring(caretPosition + 1);
        text = prefix + suffix;
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
