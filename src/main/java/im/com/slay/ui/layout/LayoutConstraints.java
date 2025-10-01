package im.com.slay.ui.layout;

import im.com.slay.ui.geometry.Insets;
import im.com.slay.ui.geometry.Vec2;

/**
 * Layout constraints mirror CSS flex or grid options. Constraints can be
 * chained with a fluent API to make configuration expressive and compact while
 * remaining type safe.
 */
public final class LayoutConstraints {

    private double grow;
    private double shrink;
    private Vec2 alignment = new Vec2(0.0, 0.0);
    private Insets margin = Insets.NONE;
    private Vec2 preferredSize = Vec2.ZERO;

    private LayoutConstraints() {
    }

    public static LayoutConstraints none() {
        return new LayoutConstraints();
    }

    public LayoutConstraints grow(double value) {
        this.grow = value;
        return this;
    }

    public LayoutConstraints shrink(double value) {
        this.shrink = value;
        return this;
    }

    public LayoutConstraints align(double x, double y) {
        this.alignment = new Vec2(x, y);
        return this;
    }

    public LayoutConstraints margin(double top, double right, double bottom, double left) {
        this.margin = new Insets(top, right, bottom, left);
        return this;
    }

    public LayoutConstraints preferredSize(double width, double height) {
        this.preferredSize = new Vec2(width, height);
        return this;
    }

    public double getGrow() {
        return grow;
    }

    public double getShrink() {
        return shrink;
    }

    public Vec2 getAlignment() {
        return alignment;
    }

    public Insets getMargin() {
        return margin;
    }

    public Vec2 getPreferredSize() {
        return preferredSize;
    }
}
