package im.com.slay.ui.math;

import java.util.Objects;

/**
 * Immutable 2D vector used for layout calculations.
 */
public final class Vec2 {

    public static final Vec2 ZERO = new Vec2(0.0, 0.0);

    private final double x;
    private final double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(this.x + other.x, this.y + other.y);
    }

    public Vec2 subtract(Vec2 other) {
        return new Vec2(this.x - other.x, this.y - other.y);
    }

    public Vec2 multiply(double scalar) {
        return new Vec2(this.x * scalar, this.y * scalar);
    }

    public Vec2 max(Vec2 other) {
        return new Vec2(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vec2)) {
            return false;
        }
        Vec2 vec2 = (Vec2) o;
        return Double.compare(vec2.x, x) == 0 && Double.compare(vec2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vec2{" + "x=" + x + ", y=" + y + '}';
    }
}
