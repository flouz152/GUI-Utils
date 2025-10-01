package im.com.slay.ui.geometry;

import im.com.slay.ui.math.Vec2;

/**
 * Simple rectangle container used for layout calculations.
 */
public final class Rect {

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
}
