package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.math.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Basic text label component.
 */
public class UILabel extends UIComponent {

    private String text;
    private int color = 0xFFFFFFFF;
    private boolean centered;
    private boolean expandHorizontally = true;

    public UILabel(String text) {
        this.text = text;
        setPreferredSize(new Vec2(160, 12));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    public boolean isCentered() {
        return centered;
    }

    public void setExpandHorizontally(boolean expandHorizontally) {
        this.expandHorizontally = expandHorizontally;
    }

    public boolean isExpandHorizontally() {
        return expandHorizontally;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        double width = expandHorizontally ? availableSize.getX() : getPreferredSize().getX();
        return new Vec2(width, getPreferredSize().getY());
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect rect) {
        if (centered) {
            renderer.drawCenteredString(text, rect.getX() + rect.getWidth() / 2.0, rect.getY() + rect.getHeight() / 2.0 - 4.0, color);
        } else {
            renderer.drawString(text, rect.getX(), rect.getY() + rect.getHeight() / 2.0 - 4.0, color);
        }
    }
}
