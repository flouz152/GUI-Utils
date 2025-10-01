package im.com.slay.ui.animation;

/**
 * Represents a single keyframe in a track. Keyframes are defined by a time
 * position and a value. The timeline can interpolate between values using
 * custom easing functions.
 */
public final class AnimationKeyframe<T> {

    private final double time;
    private final T value;

    public AnimationKeyframe(double time, T value) {
        this.time = time;
        this.value = value;
    }

    public double getTime() {
        return time;
    }

    public T getValue() {
        return value;
    }
}
