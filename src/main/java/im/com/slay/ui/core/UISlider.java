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
 * Slider control that supports animated thumb transitions and value bindings.
 * Inspired by material design sliders with soft corners.
 */
public final class UISlider extends UIComponent {

    public interface Listener {
        void onValueChanged(double value);
    }

    private double min;
    private double max = 1.0;
    private double value;
    private boolean dragging;
    private Listener listener;
    private final AnimationTimeline thumbTimeline;
    private double thumbScale = 1.0;

    public UISlider() {
        AnimationTrack<Double> scaleTrack = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * Easings.easeOutCubic(t);
            }
        });
        scaleTrack.addKeyframe(new AnimationKeyframe<Double>(0, 1.0));
        scaleTrack.addKeyframe(new AnimationKeyframe<Double>(0.12, 1.15));
        scaleTrack.addKeyframe(new AnimationKeyframe<Double>(0.32, 1.0));
        thumbTimeline = new AnimationTimeline().addTrack("scale", scaleTrack);
        thumbTimeline.addListener(new AnimationTimeline.AnimationListener() {
            @Override
            public void onTick(AnimationTimeline timeline, double time) {
                Double sampled = timeline.sample("scale", time);
                if (sampled != null) {
                    thumbScale = sampled.doubleValue();
                }
            }

            @Override
            public void onLoop(AnimationTimeline timeline) {
            }

            @Override
            public void onFinished(AnimationTimeline timeline) {
            }
        });
        addTimeline(thumbTimeline);
    }

    public UISlider range(double min, double max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public UISlider value(double value) {
        this.value = clamp(value);
        notifyListener();
        return this;
    }

    public UISlider onChange(Listener listener) {
        this.listener = listener;
        return this;
    }

    public double getValue() {
        return value;
    }

    private double clamp(double v) {
        return Math.max(min, Math.min(max, v));
    }

    public void beginDrag() {
        dragging = true;
        thumbTimeline.reset();
        thumbTimeline.play();
    }

    public void updateDrag(double normalized) {
        if (!dragging) {
            return;
        }
        double newValue = min + (max - min) * normalized;
        if (Math.abs(newValue - value) > 1e-3) {
            value = clamp(newValue);
            notifyListener();
        }
    }

    public void endDrag() {
        dragging = false;
        thumbTimeline.reset();
    }

    private void notifyListener() {
        if (listener != null) {
            listener.onValueChanged(value);
        }
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(Math.max(availableSize.getX(), 200), 32);
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        int trackColor = ColorUtil.withOpacity(getPalette().getSurfaceColor(), 0.35);
        renderer.drawRect(bounds, trackColor, 16);
        double normalized = (value - min) / (max - min);
        double filledWidth = Math.max(4, bounds.getWidth() * normalized);
        Rect filled = Rect.of(bounds.getLeft(), bounds.getTop(), filledWidth, bounds.getHeight());
        renderer.drawRect(filled, getPalette().getPrimaryColor(), 16);
        double thumbRadius = 10 * thumbScale;
        double thumbX = bounds.getLeft() + bounds.getWidth() * normalized;
        Rect thumb = Rect.of(thumbX - thumbRadius, bounds.getCenterY() - thumbRadius, thumbRadius * 2, thumbRadius * 2);
        renderer.drawCircle(thumb, getPalette().getAccentColor(), thumbScale);
    }
}
