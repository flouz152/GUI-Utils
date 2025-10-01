package im.com.slay.ui.util;

/**
 * Collection of easing helper functions used by animations.
 */
public final class Easings {

    private Easings() {
    }

    /**
     * Elastic ease out implementation. Produces a spring like motion as the value approaches 1.
     *
     * @param t progress between 0 and 1
     * @return eased progress
     */
    public static double elasticOut(double t) {
        if (t <= 0.0) {
            return 0.0;
        }
        if (t >= 1.0) {
            return 1.0;
        }

        double c4 = (2 * Math.PI) / 3.0;
        return Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1;
    }

    /**
     * Back ease out implementation. Creates an overshoot as it approaches 1.
     *
     * @param t progress between 0 and 1
     * @return eased progress
     */
    public static double easeOutBack(double t) {
        if (t <= 0.0) {
            return 0.0;
        }
        if (t >= 1.0) {
            return 1.0;
        }

        double c1 = 1.70158;
        double c3 = c1 + 1;
        double progress = t - 1.0;
        return 1 + c3 * Math.pow(progress, 3) + c1 * Math.pow(progress, 2);
    }
}
