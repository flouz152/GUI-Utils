package im.com.slay.ui.animation;

/**
 * Cubic bezier interpolator similar to Android's PathInterpolator.
 * Designers can specify custom easing curves which are sampled using a binary
 * search. Works on Java 8 and Java 21 because it only relies on math.
 */
public final class PathInterpolator {

    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    public PathInterpolator(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double interpolate(double t) {
        double clamp = Math.max(0.0, Math.min(1.0, t));
        double low = 0.0;
        double high = 1.0;
        double currentT = clamp;
        for (int i = 0; i < 12; i++) {
            double currentX = cubic(currentT, x1, x2);
            double derivative = derivative(currentT, x1, x2);
            if (Math.abs(currentX - clamp) < 0.0001) {
                break;
            }
            if (derivative == 0.0) {
                break;
            }
            currentT -= (currentX - clamp) / derivative;
            if (currentT < low || currentT > high) {
                currentT = (low + high) / 2.0;
            }
        }
        return cubic(currentT, y1, y2);
    }

    private double cubic(double t, double a1, double a2) {
        double c = 3.0 * a1;
        double b = 3.0 * (a2 - a1) - c;
        double a = 1.0 - c - b;
        return ((a * t + b) * t + c) * t;
    }

    private double derivative(double t, double a1, double a2) {
        double c = 3.0 * a1;
        double b = 3.0 * (a2 - a1) - c;
        double a = 1.0 - c - b;
        return (3.0 * a * t + 2.0 * b) * t + c;
    }
}
