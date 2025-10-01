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
    private boolean loop;

    private TimelineBuilder() {
    }

    public static TimelineBuilder timeline() {
        return new TimelineBuilder();
    }

    public TimelineBuilder loop(boolean loop) {
        this.loop = loop;
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
        timeline.loop(loop);
        return timeline;
    }
}
