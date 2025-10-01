package im.com.slay.ui.animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple scheduler driving multiple timelines with delta time updates.
 */
public final class AnimationScheduler {

    private final List<AnimationTimeline> timelines = new ArrayList<AnimationTimeline>();
    private boolean playing;

    public AnimationScheduler add(AnimationTimeline timeline) {
        if (timeline != null) {
            timelines.add(timeline);
        }
        return this;
    }

    public void remove(AnimationTimeline timeline) {
        timelines.remove(timeline);
    }

    public void clear() {
        timelines.clear();
    }

    public void playAll() {
        playing = true;
    }

    public void stopAll() {
        playing = false;
    }

    public void update(double deltaTime) {
        if (!playing) {
            return;
        }
        Iterator<AnimationTimeline> iterator = timelines.iterator();
        while (iterator.hasNext()) {
            AnimationTimeline timeline = iterator.next();
            timeline.update(deltaTime);
            if (timeline.isFinished()) {
                iterator.remove();
            }
        }
    }
}
