package im.com.slay.ui.geometry;

import im.com.slay.ui.math.Vec2;

/**
 * Simple rectangle container used for layout calculations.
 */
public final class Rect {

    public static final Rect EMPTY = new Rect(0.0, 0.0, 0.0, 0.0);

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public Rect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Vec2 getSize() {
        return new Vec2(width, height);
    }

    public Rect inset(double padding) {
        return inset(padding, padding);
    }

    public Rect inset(double horizontal, double vertical) {
        double newX = x + horizontal;
        double newY = y + vertical;
        double newWidth = Math.max(0.0, width - horizontal * 2.0);
        double newHeight = Math.max(0.0, height - vertical * 2.0);
        return new Rect(newX, newY, newWidth, newHeight);
    }

    public boolean contains(double pointX, double pointY) {
        if (width <= 0.0 || height <= 0.0) {
            return false;
        }
        return pointX >= x && pointY >= y && pointX <= x + width && pointY <= y + height;
    }
}
