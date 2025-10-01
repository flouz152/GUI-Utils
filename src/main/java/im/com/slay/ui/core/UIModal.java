package im.com.slay.ui.core;

import im.com.slay.ui.animation.AnimationTimeline;
import im.com.slay.ui.animation.AnimationTrack;
import im.com.slay.ui.animation.Easings;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.theme.ColorUtil;

/**
 * Full screen modal overlay with fade and scale animations. Useful for dialogs.
 */
public final class UIModal extends UIContainer {

    private double alpha = 0.0;
    private double scale = 0.9;
    private final AnimationTimeline fadeTimeline;

    public UIModal() {
        super(FlexLayout.vertical());
        AnimationTrack<Double> fadeTrack = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * Easings.easeOutQuad(t);
            }
        });
        fadeTrack.addKeyframe(new im.com.slay.ui.animation.AnimationKeyframe<Double>(0, 0.0));
        fadeTrack.addKeyframe(new im.com.slay.ui.animation.AnimationKeyframe<Double>(0.25, 1.0));
        AnimationTrack<Double> scaleTrack = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * Easings.easeOutBack(t);
            }
        });
        scaleTrack.addKeyframe(new im.com.slay.ui.animation.AnimationKeyframe<Double>(0, 0.85));
        scaleTrack.addKeyframe(new im.com.slay.ui.animation.AnimationKeyframe<Double>(0.25, 1.0));
        fadeTimeline = new AnimationTimeline()
                .addTrack("alpha", fadeTrack)
                .addTrack("scale", scaleTrack);
        fadeTimeline.addListener(new AnimationTimeline.AnimationListener() {
            @Override
            public void onTick(AnimationTimeline timeline, double time) {
                Double a = timeline.sample("alpha", time);
                Double s = timeline.sample("scale", time);
                if (a != null) {
                    alpha = a.doubleValue();
                }
                if (s != null) {
                    scale = s.doubleValue();
                }
            }

            @Override
            public void onLoop(AnimationTimeline timeline) {
            }

            @Override
            public void onFinished(AnimationTimeline timeline) {
            }
        });
        addTimeline(fadeTimeline);
    }

    public void show() {
        alpha = 0.0;
        scale = 0.9;
        fadeTimeline.reset();
        fadeTimeline.play();
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return availableSize;
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        int overlay = ColorUtil.withOpacity(getPalette().getBackgroundColor(), alpha * 0.75);
        renderer.drawRect(bounds, overlay, 0);
        renderer.translate(bounds.getCenterX(), bounds.getCenterY(), 0);
        renderer.scale(scale, scale, 1);
        renderer.translate(-bounds.getCenterX(), -bounds.getCenterY(), 0);
    }
}
