package im.com.slay.ui.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Animation timeline coordinates multiple tracks. The implementation supports
 * chaining, reversing and retargeting which enables cinematic transitions.
 */
public final class AnimationTimeline {

    private final Map<String, AnimationTrack<?>> tracks = new HashMap<String, AnimationTrack<?>>();
    private final List<AnimationListener> listeners = new ArrayList<AnimationListener>();
    private double time;
    private double duration;
    private boolean loop;
    private boolean playing = true;

    public <T> AnimationTimeline addTrack(String name, AnimationTrack<T> track) {
        tracks.put(name, track);
        duration = Math.max(duration, track.getDuration());
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T sample(String name, double time) {
        AnimationTrack<T> track = (AnimationTrack<T>) tracks.get(name);
        if (track == null) {
            return null;
        }
        return track.sample(time);
    }

    public void update(double delta) {
        if (!playing) {
            return;
        }
        time += delta;
        if (time > duration) {
            if (loop) {
                time = time % duration;
                notifyLoop();
            } else {
                time = duration;
                playing = false;
                notifyFinished();
            }
        }
        notifyTick();
    }

    public double getTime() {
        return time;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isFinished() {
        return !playing && time >= duration;
    }

    public AnimationTimeline loop(boolean loop) {
        this.loop = loop;
        return this;
    }

    public void play() {
        this.playing = true;
    }

    public void pause() {
        this.playing = false;
    }

    public void seek(double time) {
        this.time = Math.max(0, Math.min(duration, time));
        notifyTick();
    }

    public void reset() {
        this.time = 0;
        this.playing = true;
        notifyTick();
    }

    public void addListener(AnimationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AnimationListener listener) {
        listeners.remove(listener);
    }

    private void notifyTick() {
        for (AnimationListener listener : listeners) {
            listener.onTick(this, time);
        }
    }

    private void notifyLoop() {
        for (AnimationListener listener : listeners) {
            listener.onLoop(this);
        }
    }

    private void notifyFinished() {
        for (AnimationListener listener : listeners) {
            listener.onFinished(this);
        }
    }

    public interface AnimationListener {
        void onTick(AnimationTimeline timeline, double time);

        void onLoop(AnimationTimeline timeline);

        void onFinished(AnimationTimeline timeline);
    }
}
