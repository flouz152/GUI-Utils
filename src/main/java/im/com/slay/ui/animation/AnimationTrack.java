package im.com.slay.ui.animation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A track is a collection of keyframes that animate a single property. Tracks
 * can operate on arbitrary value types as long as a {@link Interpolator}
 * implementation is supplied.
 */
public final class AnimationTrack<T> {

    private final List<AnimationKeyframe<T>> keyframes = new ArrayList<AnimationKeyframe<T>>();
    private Interpolator<T> interpolator;

    public AnimationTrack(Interpolator<T> interpolator) {
        this.interpolator = interpolator;
    }

    public AnimationTrack<T> addKeyframe(AnimationKeyframe<T> keyframe) {
        keyframes.add(keyframe);
        Collections.sort(keyframes, new Comparator<AnimationKeyframe<T>>() {
            @Override
            public int compare(AnimationKeyframe<T> o1, AnimationKeyframe<T> o2) {
                return Double.compare(o1.getTime(), o2.getTime());
            }
        });
        return this;
    }

    public T sample(double time) {
        if (keyframes.isEmpty()) {
            return null;
        }
        AnimationKeyframe<T> previous = keyframes.get(0);
        for (int i = 1; i < keyframes.size(); i++) {
            AnimationKeyframe<T> current = keyframes.get(i);
            if (time < current.getTime()) {
                double t = (time - previous.getTime()) / (current.getTime() - previous.getTime());
                t = Math.max(0, Math.min(1, t));
                return interpolator.interpolate(previous.getValue(), current.getValue(), t);
            }
            previous = current;
        }
        return keyframes.get(keyframes.size() - 1).getValue();
    }

    public double getDuration() {
        if (keyframes.isEmpty()) {
            return 0;
        }
        return keyframes.get(keyframes.size() - 1).getTime();
    }

    public interface Interpolator<T> {
        T interpolate(T start, T end, double t);
    }
}
