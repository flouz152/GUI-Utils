package im.com.slay.ui.animation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Fluent builder simplifying creation of {@link AnimationTimeline} instances
 * with multiple tracks. Inspired by Framer Motion timelines where designers
 * describe keyframes declaratively and reuse them across widgets.
 */
public final class TimelineBuilder {

    private final Map<String, AnimationTrack<?>> tracks = new LinkedHashMap<String, AnimationTrack<?>>();
    private AnimationTimeline.LoopMode loopMode = AnimationTimeline.LoopMode.NONE;
    private double playbackSpeed = 1.0;
    private boolean autoPlay = true;
    private double startTime = 0.0;
    private boolean startForward = true;

    private TimelineBuilder() {
    }

    public static TimelineBuilder timeline() {
        return new TimelineBuilder();
    }

    public TimelineBuilder loop(boolean loop) {
        this.loopMode = loop ? AnimationTimeline.LoopMode.RESTART : AnimationTimeline.LoopMode.NONE;
        return this;
    }

    public TimelineBuilder loopMode(AnimationTimeline.LoopMode mode) {
        this.loopMode = mode == null ? AnimationTimeline.LoopMode.NONE : mode;
        return this;
    }

    public TimelineBuilder pingPong(boolean enable) {
        this.loopMode = enable ? AnimationTimeline.LoopMode.PING_PONG : AnimationTimeline.LoopMode.NONE;
        return this;
    }

    public TimelineBuilder playbackSpeed(double speed) {
        this.playbackSpeed = Math.max(0.0001, speed);
        return this;
    }

    public TimelineBuilder autoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        return this;
    }

    public TimelineBuilder startTime(double startTime) {
        this.startTime = Math.max(0.0, startTime);
        return this;
    }

    public TimelineBuilder startForward(boolean startForward) {
        this.startForward = startForward;
        return this;
    }

    public <T> TimelineBuilder track(String name, AnimationTrack<T> track) {
        tracks.put(name, track);
        return this;
    }

    public AnimationTimeline build() {
        AnimationTimeline timeline = new AnimationTimeline();
        for (Entry<String, AnimationTrack<?>> entry : tracks.entrySet()) {
            timeline.addTrack(entry.getKey(), entry.getValue());
        }
        timeline.loopMode(loopMode);
        timeline.playbackSpeed(playbackSpeed);
        timeline.direction(startForward);
        timeline.seek(startTime);
        if (!autoPlay) {
            timeline.pause();
        }
        return timeline;
    }
}
