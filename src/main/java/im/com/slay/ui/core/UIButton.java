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

    private String text;
    private Consumer<UIButton> onClick;
    private boolean hovered;
    private double hoverProgress;

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

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return getPreferredSize();
    }

    @Override
    public void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        double target = hovered ? 1.0 : 0.0;
        double delta = target - hoverProgress;
        hoverProgress += delta * 0.2;
        double eased = Easings.elasticOut(hoverProgress);

        int baseColor = 0x55000000;
        int highlight = (int) (0x55 * eased) << 24;
        renderer.fillRect(rect, baseColor + highlight);
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
