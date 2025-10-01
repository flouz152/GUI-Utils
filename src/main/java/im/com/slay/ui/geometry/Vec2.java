package im.com.slay.ui.geometry;

/**
 * Immutable two dimensional vector used across the GUI toolkit. The
 * implementation intentionally mirrors common vector APIs from game engines
 * such as Unity or Unreal so that designers coming from those ecosystems can
 * immediately apply their knowledge. Operations never mutate the current
 * instance which keeps state flows easier to reason about when combined with
 * reactive binding utilities.
 */
public final class Vec2 {

    public static final Vec2 ZERO = new Vec2(0.0, 0.0);
    public static final Vec2 ONE = new Vec2(1.0, 1.0);

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

    public Vec2 multiply(Vec2 other) {
        return new Vec2(this.x * other.x, this.y * other.y);
    }

    public Vec2 divide(double scalar) {
        if (scalar == 0.0) {
            return ZERO;
        }
        return new Vec2(this.x / scalar, this.y / scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vec2 normalized() {
        double length = length();
        if (length == 0.0) {
            return ZERO;
        }
        return divide(length);
    }

    public double dot(Vec2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vec2 lerp(Vec2 target, double t) {
        double nx = this.x + (target.x - this.x) * t;
        double ny = this.y + (target.y - this.y) * t;
        return new Vec2(nx, ny);
    }

    public Vec2 clamp(Vec2 min, Vec2 max) {
        double nx = Math.max(min.x, Math.min(max.x, this.x));
        double ny = Math.max(min.y, Math.min(max.y, this.y));
        return new Vec2(nx, ny);
    }

    public Vec2 withX(double newX) {
        return new Vec2(newX, this.y);
    }

    public Vec2 withY(double newY) {
        return new Vec2(this.x, newY);
    }

    @Override
    public String toString() {
        return "Vec2{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vec2)) {
            return false;
        }
        Vec2 other = (Vec2) obj;
        return Double.compare(other.x, x) == 0 && Double.compare(other.y, y) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(x);
        int result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
