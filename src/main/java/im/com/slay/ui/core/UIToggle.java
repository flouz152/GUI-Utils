package im.com.slay.ui.core;

import im.com.slay.ui.animation.AnimationKeyframe;
import im.com.slay.ui.animation.AnimationTimeline;
import im.com.slay.ui.animation.AnimationTrack;
import im.com.slay.ui.animation.Easings;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Animated toggle switch with springy transitions reminiscent of iOS controls.
 */
public final class UIToggle extends UIComponent {

    public interface Listener {
        void onToggled(boolean enabled);
    }

    private boolean enabled;
    private Listener listener;
    private final AnimationTimeline switchTimeline;
    private double progress;

    public UIToggle() {
        AnimationTrack<Double> track = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * Easings.easeOutBack(t);
            }
        });
        track.addKeyframe(new AnimationKeyframe<Double>(0, 0.0));
        track.addKeyframe(new AnimationKeyframe<Double>(0.3, 1.0));
        switchTimeline = new AnimationTimeline().addTrack("progress", track);
        switchTimeline.addListener(new AnimationTimeline.AnimationListener() {
            @Override
            public void onTick(AnimationTimeline timeline, double time) {
                Double sample = timeline.sample("progress", time);
                if (sample != null) {
                    progress = sample.doubleValue();
                }
            }

            @Override
            public void onLoop(AnimationTimeline timeline) {
            }

            @Override
            public void onFinished(AnimationTimeline timeline) {
            }
        });
        addTimeline(switchTimeline);
    }

    public UIToggle state(boolean enabled) {
        this.enabled = enabled;
        progress = enabled ? 1.0 : 0.0;
        return this;
    }

    public UIToggle onToggle(Listener listener) {
        this.listener = listener;
        return this;
    }

    public void toggle() {
        enabled = !enabled;
        progress = enabled ? 1.0 : 0.0;
        switchTimeline.reset();
        switchTimeline.play();
        if (listener != null) {
            listener.onToggled(enabled);
        }
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(52, 28);
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        int trackColor = enabled ? getPalette().getPrimaryColor()
                : ColorUtil.withOpacity(getPalette().getSurfaceColor(), 0.45);
        renderer.drawRect(bounds, trackColor, bounds.getHeight() / 2.0);
        double knobRadius = bounds.getHeight() / 2.4;
        double knobX = bounds.getLeft() + (bounds.getWidth() - knobRadius * 2) * progress;
        Rect knob = Rect.of(knobX, bounds.getTop() + (bounds.getHeight() - knobRadius * 2) / 2.0,
                knobRadius * 2, knobRadius * 2);
        renderer.drawCircle(knob, getPalette().getBackgroundColor(), 1.0);
    }
}
