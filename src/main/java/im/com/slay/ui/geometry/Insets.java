package im.com.slay.ui.geometry;

/**
 * Represents padding or margin values in a component. Insets are immutable and
 * provide helpers for combining and scaling values which is useful for
 * implementing responsive layouts.
 */
public final class Insets {

    public static final Insets NONE = new Insets(0, 0, 0, 0);

    private final double top;
    private final double right;
    private final double bottom;
    private final double left;

    public Insets(double top, double right, double bottom, double left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public double getRight() {
        return right;
    }

    public double getBottom() {
        return bottom;
    }

    public double getLeft() {
        return left;
    }

    public Insets add(Insets other) {
        return new Insets(
                this.top + other.top,
                this.right + other.right,
                this.bottom + other.bottom,
                this.left + other.left
        );
    }

    public Insets multiply(double scalar) {
        return new Insets(top * scalar, right * scalar, bottom * scalar, left * scalar);
    }

    public double getHorizontal() {
        return left + right;
    }

    public double getVertical() {
        return top + bottom;
    }

    public Insets symmetrical(double vertical, double horizontal) {
        return new Insets(vertical, horizontal, vertical, horizontal);
    }

    @Override
    public String toString() {
        return "Insets{" + "top=" + top + ", right=" + right + ", bottom=" + bottom + ", left=" + left + '}';
    }
}
