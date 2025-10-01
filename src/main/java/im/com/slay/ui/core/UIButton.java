package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.util.Easings;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Simple button component with a text label and an optional click handler.
 */
public class UIButton extends UIComponent {

    public enum AnimationStyle {
        ELASTIC,
        BACK,
        LINEAR
    }

    private String text;
    private Consumer<UIButton> onClick;
    private boolean hovered;
    private double hoverProgress;
    private AnimationStyle animationStyle = AnimationStyle.ELASTIC;
    private int backgroundColor = 0x66000000;
    private int highlightColor = 0x3300FFFF;
    private int textColor = 0xFFFFFFFF;

    public UIButton(String text) {
        this.text = text;
        setPreferredSize(new Vec2(80, 20));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOnClick(Consumer<UIButton> onClick) {
        this.onClick = onClick;
    }

    public void click() {
        if (onClick != null) {
            onClick.accept(this);
        }
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setAnimationStyle(AnimationStyle animationStyle) {
        this.animationStyle = animationStyle;
    }

    public AnimationStyle getAnimationStyle() {
        return animationStyle;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return getPreferredSize();
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        double target = hovered ? 1.0 : 0.0;
        double delta = target - hoverProgress;
        hoverProgress += delta * 0.2;
        hoverProgress = Math.max(0.0, Math.min(1.0, hoverProgress));

        double eased = applyEasing(hoverProgress);

        int alpha = (int) (Math.min(1.0, eased) * 255.0);
        int blendedHighlight = (highlightColor & 0x00FFFFFF) | (alpha << 24);

        renderer.fillRect(rect, backgroundColor);
        renderer.fillRect(rect, blendedHighlight);
        renderer.drawCenteredString(text, rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() / 2.0 - 4.0, textColor);
    }

    private double applyEasing(double value) {
        switch (animationStyle) {
            case BACK:
                return Easings.easeOutBack(value);
            case LINEAR:
                return value;
            case ELASTIC:
            default:
                return Easings.elasticOut(value);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UIButton)) {
            return false;
        }
        UIButton other = (UIButton) obj;
        return Objects.equals(text, other.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
