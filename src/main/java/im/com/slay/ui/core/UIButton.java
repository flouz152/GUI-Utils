package im.com.slay.ui.core;

import im.com.slay.ui.animation.AnimationTimeline;
import im.com.slay.ui.animation.AnimationTrack;
import im.com.slay.ui.animation.Easings;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Button component that animates hover and press states using the animation
 * system. Designed to mimic modern neumorphism with soft scale and color
 * changes.
 */
public final class UIButton extends UIComponent {

    public interface ClickListener {
        void onClick();
    }

    private final UILabel label;
    private double cornerRadius = 8;
    private double scale = 1.0;
    private int backgroundColor = 0xD01C1C1C;
    private ClickListener listener;

    public UIButton(String text) {
        this.label = new UILabel(text);
        addChild(label);
        AnimationTrack<Double> scaleTrack = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * Easings.backOut(t);
            }
        });
        scaleTrack.addKeyframe(new im.com.slay.ui.animation.AnimationKeyframe<Double>(0, 0.9));
        scaleTrack.addKeyframe(new im.com.slay.ui.animation.AnimationKeyframe<Double>(0.18, 1.0));
        AnimationTimeline timeline = new AnimationTimeline().addTrack("scale", scaleTrack);
        timeline.play();
        addTimeline(timeline);
    }

    public UIButton onClick(ClickListener listener) {
        this.listener = listener;
        return this;
    }

    public UIButton cornerRadius(double cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public UIButton backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(Math.min(availableSize.getX(), 160), 36);
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        renderer.drawRect(bounds, backgroundColor, cornerRadius);
        Vec2 labelPos = new Vec2(bounds.getLeft() + 12, bounds.getTop() + 10);
        label.setPosition(labelPos);
        label.render(context, renderer);
    }

    public void click() {
        if (listener != null) {
            listener.onClick();
        }
    }
}
