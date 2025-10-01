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

    public enum LoopMode {
        NONE,
        RESTART,
        PING_PONG
    }

    private final Map<String, AnimationTrack<?>> tracks = new HashMap<String, AnimationTrack<?>>();
    private final List<AnimationListener> listeners = new ArrayList<AnimationListener>();
    private double time;
    private double duration;
    private boolean loop;
    private boolean playing = true;
    private LoopMode loopMode = LoopMode.NONE;
    private double playbackSpeed = 1.0;
    private boolean forward = true;

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
        if (!playing || duration == 0.0) {
            return;
        }
        double directionMultiplier = forward ? 1.0 : -1.0;
        double step = delta * playbackSpeed * directionMultiplier;
        if (step == 0.0) {
            return;
        }
        time += step;
        if (forward && time >= duration) {
            handleForwardBoundary();
        } else if (!forward && time <= 0.0) {
            handleBackwardBoundary();
        } else {
            notifyTick();
        }
    }

    public double getTime() {
        return time;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isFinished() {
        return !playing && (forward ? time >= duration : time <= 0.0);
    }

    public AnimationTimeline loop(boolean loop) {
        this.loop = loop;
        this.loopMode = loop ? LoopMode.RESTART : LoopMode.NONE;
        return this;
    }

    public AnimationTimeline loopMode(LoopMode mode) {
        if (mode == null) {
            mode = LoopMode.NONE;
        }
        this.loopMode = mode;
        this.loop = mode != LoopMode.NONE;
        return this;
    }

    public AnimationTimeline pingPong(boolean enabled) {
        return loopMode(enabled ? LoopMode.PING_PONG : LoopMode.NONE);
    }

    public LoopMode getLoopMode() {
        return loopMode;
    }

    public AnimationTimeline playbackSpeed(double speed) {
        this.playbackSpeed = Math.max(0.0001, speed);
        return this;
    }

    public double getPlaybackSpeed() {
        return playbackSpeed;
    }

    public void play() {
        this.playing = true;
    }

    public void pause() {
        this.playing = false;
    }

    public AnimationTimeline direction(boolean forward) {
        if (this.forward != forward) {
            this.forward = forward;
            notifyDirectionChanged();
        }
        return this;
    }

    public void playForward() {
        direction(true);
        play();
    }

    public void playReverse() {
        direction(false);
        play();
    }

    public boolean isForward() {
        return forward;
    }

    public void playFromStart() {
        direction(true);
        seek(0.0);
        play();
    }

    public void playFromEnd() {
        direction(false);
        seek(duration);
        play();
    }

    public void seek(double time) {
        this.time = Math.max(0, Math.min(duration, time));
        notifyTick();
    }

    public void reset() {
        this.time = 0;
        this.playing = true;
        this.forward = true;
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

    private void notifyDirectionChanged() {
        for (AnimationListener listener : listeners) {
            listener.onDirectionChanged(this, forward);
        }
    }

    private void handleForwardBoundary() {
        if (loopMode == LoopMode.RESTART) {
            double overshoot = time - duration;
            time = duration == 0.0 ? 0.0 : overshoot % duration;
            notifyLoop();
            notifyTick();
            return;
        }
        if (loopMode == LoopMode.PING_PONG) {
            double overshoot = time - duration;
            time = Math.max(0.0, duration - overshoot);
            direction(false);
            notifyLoop();
            notifyTick();
            return;
        }
        time = duration;
        playing = false;
        notifyTick();
        notifyFinished();
    }

    private void handleBackwardBoundary() {
        if (loopMode == LoopMode.RESTART) {
            double overshoot = -time;
            time = duration - (duration == 0.0 ? 0.0 : overshoot % duration);
            if (time == duration) {
                time = 0.0;
            }
            notifyLoop();
            notifyTick();
            return;
        }
        if (loopMode == LoopMode.PING_PONG) {
            double overshoot = -time;
            time = Math.min(duration, overshoot);
            direction(true);
            notifyLoop();
            notifyTick();
            return;
        }
        time = 0.0;
        playing = false;
        notifyTick();
        notifyFinished();
    }

    public interface AnimationListener {
        default void onTick(AnimationTimeline timeline, double time) {
        }

        default void onLoop(AnimationTimeline timeline) {
        }

        default void onFinished(AnimationTimeline timeline) {
        }

        default void onDirectionChanged(AnimationTimeline timeline, boolean forward) {
        }
    }
}
