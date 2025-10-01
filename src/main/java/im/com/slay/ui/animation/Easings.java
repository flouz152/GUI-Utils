package im.com.slay.ui.animation;

/**
 * Collection of easing curves inspired by Framer Motion. Implemented manually
 * so we avoid dependency on external libraries. These methods are pure and can
 * be used by animation tracks or shader effects.
 */
public final class Easings {

    private Easings() {
    }

    public static double linear(double t) {
        return t;
    }

    public static double easeInQuad(double t) {
        return t * t;
    }

    public static double easeOutQuad(double t) {
        return t * (2 - t);
    }

    public static double easeInOutQuad(double t) {
        if ((t *= 2) < 1) {
            return 0.5 * t * t;
        }
        return -0.5 * ((--t) * (t - 2) - 1);
    }

    public static double easeInCubic(double t) {
        return t * t * t;
    }

    public static double easeOutCubic(double t) {
        return (--t) * t * t + 1;
    }

    public static double easeInOutCubic(double t) {
        if ((t *= 2) < 1) {
            return 0.5 * t * t * t;
        }
        t -= 2;
        return 0.5 * (t * t * t + 2);
    }

    public static double easeInQuart(double t) {
        return t * t * t * t;
    }

    public static double easeOutQuart(double t) {
        return 1 - (--t) * t * t * t;
    }

    public static double easeInOutQuart(double t) {
        if ((t *= 2) < 1) {
            return 0.5 * t * t * t * t;
        }
        t -= 2;
        return -0.5 * (t * t * t * t - 2);
    }

    public static double easeInQuint(double t) {
        return t * t * t * t * t;
    }

    public static double easeOutQuint(double t) {
        return 1 + (--t) * t * t * t * t;
    }

    public static double easeInOutQuint(double t) {
        if ((t *= 2) < 1) {
            return 0.5 * t * t * t * t * t;
        }
        t -= 2;
        return 0.5 * (t * t * t * t * t + 2);
    }

    public static double easeInSine(double t) {
        return 1 - Math.cos((t * Math.PI) / 2);
    }

    public static double easeOutSine(double t) {
        return Math.sin((t * Math.PI) / 2);
    }

    public static double easeInOutSine(double t) {
        return -(Math.cos(Math.PI * t) - 1) / 2;
    }

    public static double easeInExpo(double t) {
        return t == 0 ? 0 : Math.pow(2, 10 * t - 10);
    }

    public static double easeOutExpo(double t) {
        return t == 1 ? 1 : 1 - Math.pow(2, -10 * t);
    }

    public static double easeInOutExpo(double t) {
        if (t == 0) {
            return 0;
        }
        if (t == 1) {
            return 1;
        }
        if ((t *= 2) < 1) {
            return Math.pow(2, 10 * (t - 1)) / 2;
        }
        return (2 - Math.pow(2, -10 * (t - 1))) / 2;
    }

    public static double easeInElastic(double t) {
        return easeInElastic(t, 1, 0.3);
    }

    public static double easeInElastic(double t, double amplitude, double period) {
        if (t == 0 || t == 1) {
            return t;
        }
        double s = period / (2 * Math.PI) * Math.asin(1 / amplitude);
        return -(amplitude * Math.pow(2, 10 * (t - 1)) * Math.sin((t - 1 - s) * (2 * Math.PI) / period));
    }

    public static double easeOutElastic(double t) {
        return easeOutElastic(t, 1, 0.3);
    }

    public static double easeOutElastic(double t, double amplitude, double period) {
        if (t == 0 || t == 1) {
            return t;
        }
        double s = period / (2 * Math.PI) * Math.asin(1 / amplitude);
        return amplitude * Math.pow(2, -10 * t) * Math.sin((t - s) * (2 * Math.PI) / period) + 1;
    }

    public static double easeInOutElastic(double t) {
        return easeInOutElastic(t, 1, 0.45);
    }

    public static double easeInOutElastic(double t, double amplitude, double period) {
        if (t == 0 || t == 1) {
            return t;
        }
        double s = period / (2 * Math.PI) * Math.asin(1 / amplitude);
        if ((t *= 2) < 1) {
            return -0.5 * (amplitude * Math.pow(2, 10 * (t - 1)) * Math.sin((t - 1 - s) * (2 * Math.PI) / period));
        }
        return amplitude * Math.pow(2, -10 * (t - 1)) * Math.sin((t - 1 - s) * (2 * Math.PI) / period) * 0.5 + 1;
    }

    public static double backIn(double t) {
        double s = 1.70158;
        return t * t * ((s + 1) * t - s);
    }

    public static double backOut(double t) {
        double s = 1.70158;
        return --t * t * ((s + 1) * t + s) + 1;
    }

    public static double backInOut(double t) {
        double s = 1.70158 * 1.525;
        if ((t *= 2) < 1) {
            return 0.5 * (t * t * ((s + 1) * t - s));
        }
        t -= 2;
        return 0.5 * (t * t * ((s + 1) * t + s) + 2);
    }

    public static double bounceOut(double t) {
        double n1 = 7.5625;
        double d1 = 2.75;
        if (t < 1 / d1) {
            return n1 * t * t;
        } else if (t < 2 / d1) {
            return n1 * (t -= 1.5 / d1) * t + 0.75;
        } else if (t < 2.5 / d1) {
            return n1 * (t -= 2.25 / d1) * t + 0.9375;
        }
        return n1 * (t -= 2.625 / d1) * t + 0.984375;
    }

    public static double bounceIn(double t) {
        return 1 - bounceOut(1 - t);
    }

    public static double bounceInOut(double t) {
        if (t < 0.5) {
            return (1 - bounceOut(1 - 2 * t)) / 2;
        }
        return (1 + bounceOut(2 * t - 1)) / 2;
    }
}
