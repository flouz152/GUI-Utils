package im.com.slay.ui.core;

import im.com.slay.ui.animation.AnimationTimeline;
import im.com.slay.ui.animation.AnimationTrack;
import im.com.slay.ui.animation.Easings;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Simple text label that supports animated opacity and dynamic scaling. Labels
 * are essential building blocks and demonstrate how the declarative state API
 * works in practice.
 */
public final class UILabel extends UIComponent {

    private String text;
    private int color = 0xFFFFFFFF;
    private double fontSize = 16;
    private double opacity = 1.0;

    public UILabel(String text) {
        this.text = text;
        AnimationTrack<Double> opacityTrack = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                double eased = Easings.easeInOutCubic(t);
                return start + (end - start) * eased;
            }
        });
        opacityTrack.addKeyframe(new im.com.slay.ui.animation.AnimationKeyframe<Double>(0, 0.0));
        opacityTrack.addKeyframe(new im.com.slay.ui.animation.AnimationKeyframe<Double>(0.35, 1.0));
        AnimationTimeline timeline = new AnimationTimeline().addTrack("opacity", opacityTrack);
        timeline.play();
        addTimeline(timeline);
    }

    public UILabel color(int color) {
        this.color = color;
        return this;
    }

    public UILabel fontSize(double fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public UILabel opacity(double opacity) {
        this.opacity = opacity;
        return this;
    }

    public UILabel text(String text) {
        this.text = text;
        return this;
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(availableSize.getX(), fontSize + 4);
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        renderer.drawText(text, bounds.getPosition(), color, fontSize, 0.35 * opacity);
    }
}
