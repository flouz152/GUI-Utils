package im.com.slay.ui.core;

import im.com.slay.ui.animation.AnimationKeyframe;
import im.com.slay.ui.animation.AnimationTimeline;
import im.com.slay.ui.animation.AnimationTrack;
import im.com.slay.ui.animation.Easings;
import im.com.slay.ui.animation.TimelineBuilder;
import im.com.slay.ui.effects.RippleEffect;
import im.com.slay.ui.geometry.Insets;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.render.SurfaceRendererExtensions;
import im.com.slay.ui.theme.ColorUtil;
import im.com.slay.ui.theme.ThemePalette;

/**
 * Animated button that showcases the richer animation feature set. Hover and
 * press states are blended using dedicated timelines while a ripple effect is
 * fired on clicks, mimicking modern material and glass morph interfaces.
 */
public final class UIButton extends UIComponent {

    public interface ClickListener {
        void onClick();
    }

    public enum InteractionState {
        IDLE,
        HOVERED,
        PRESSED
    }

    private final UILabel label;
    private final RippleEffect pressRipple = new RippleEffect().rings(2).thickness(9.0).radiusMultiplier(0.7);
    private final AnimationTimeline hoverTimeline;
    private final AnimationTimeline pressTimeline;
    private final AnimationTimeline rippleTimeline;
    private double cornerRadius = 12;
    private int backgroundColor = 0xD0242424;
    private int accentColor = 0xFF4DA8FF;
    private int textColor = 0xFFFFFFFF;
    private double hoverProgress;
    private double pressProgress;
    private double rippleProgress;
    private double rippleFade;
    private boolean backgroundExplicit;
    private boolean accentExplicit;
    private boolean textExplicit;
    private boolean selected;
    private InteractionState interactionState = InteractionState.IDLE;
    private ClickListener listener;

    public UIButton(String text) {
        this.label = new UILabel(text);
        addChild(label);
        hoverTimeline = TimelineBuilder.timeline()
                .autoPlay(false)
                .track("hover", createHoverTrack())
                .build();
        hoverTimeline.seek(0.0);
        hoverTimeline.pause();
        hoverTimeline.addListener(new AnimationTimeline.AnimationListener() {
            @Override
            public void onTick(AnimationTimeline timeline, double time) {
                Double value = timeline.sample("hover", time);
                if (value != null) {
                    hoverProgress = value.doubleValue();
                }
            }
        });
        addTimeline(hoverTimeline);

        pressTimeline = TimelineBuilder.timeline()
                .autoPlay(false)
                .track("press", createPressTrack())
                .build();
        pressTimeline.seek(0.0);
        pressTimeline.pause();
        pressTimeline.addListener(new AnimationTimeline.AnimationListener() {
            @Override
            public void onTick(AnimationTimeline timeline, double time) {
                Double value = timeline.sample("press", time);
                if (value != null) {
                    pressProgress = value.doubleValue();
                }
            }

            @Override
            public void onFinished(AnimationTimeline timeline) {
                timeline.pause();
                timeline.seek(0.0);
            }
        });
        addTimeline(pressTimeline);

        rippleTimeline = TimelineBuilder.timeline()
                .autoPlay(false)
                .track("progress", createRippleProgressTrack())
                .track("fade", createRippleFadeTrack())
                .build();
        rippleTimeline.seek(0.0);
        rippleTimeline.pause();
        rippleTimeline.addListener(new AnimationTimeline.AnimationListener() {
            @Override
            public void onTick(AnimationTimeline timeline, double time) {
                Double progress = timeline.sample("progress", time);
                Double fade = timeline.sample("fade", time);
                if (progress != null) {
                    rippleProgress = progress.doubleValue();
                }
                if (fade != null) {
                    rippleFade = fade.doubleValue();
                }
            }

            @Override
            public void onFinished(AnimationTimeline timeline) {
                timeline.pause();
                timeline.seek(0.0);
                rippleFade = 0.0;
                rippleProgress = 0.0;
            }
        });
        addTimeline(rippleTimeline);
    }

    public UIButton onClick(ClickListener listener) {
        this.listener = listener;
        return this;
    }

    public UIButton cornerRadius(double cornerRadius) {
        this.cornerRadius = Math.max(0.0, cornerRadius);
        return this;
    }

    public UIButton backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundExplicit = true;
        return this;
    }

    public UIButton accentColor(int accentColor) {
        this.accentColor = accentColor;
        this.accentExplicit = true;
        return this;
    }

    public UIButton textColor(int textColor) {
        this.textColor = textColor;
        this.textExplicit = true;
        return this;
    }

    public UIButton text(String text) {
        label.text(text);
        return this;
    }

    public UIButton selected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setInteractionState(InteractionState state) {
        if (state == null) {
            state = InteractionState.IDLE;
        }
        if (interactionState == state) {
            return;
        }
        interactionState = state;
        switch (state) {
            case HOVERED:
                hoverTimeline.playForward();
                break;
            case PRESSED:
                playPressFeedback();
                break;
            case IDLE:
            default:
                hoverTimeline.playReverse();
                break;
        }
    }

    public void setHovered(boolean hovered) {
        setInteractionState(hovered ? InteractionState.HOVERED : InteractionState.IDLE);
    }

    public void setPressed(boolean pressed) {
        if (pressed) {
            setInteractionState(InteractionState.PRESSED);
        } else {
            setInteractionState(selected ? InteractionState.HOVERED : InteractionState.IDLE);
        }
    }

    public void playPressFeedback() {
        pressTimeline.playFromStart();
        rippleTimeline.playFromStart();
    }

    public void pulse() {
        rippleTimeline.playFromStart();
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(Math.min(availableSize.getX(), 176), 38);
    }

    @Override
    protected void onThemeChanged(ThemePalette palette) {
        if (!backgroundExplicit) {
            backgroundColor = ColorUtil.blend(palette.getSurfaceColor(), palette.getPrimaryColor(), 0.28);
        }
        if (!accentExplicit) {
            accentColor = palette.getAccentColor();
        }
        if (!textExplicit) {
            textColor = palette.getTextPrimaryColor();
        }
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        double scale = 1.0 + hoverProgress * 0.06 - pressProgress * 0.04;
        double elevation = 6.0 + hoverProgress * 8.0 - pressProgress * 4.0;
        double sheen = 0.28 + hoverProgress * 0.45;
        int base = selected ? ColorUtil.lighten(backgroundColor, 0.1) : backgroundColor;
        int hoverTint = ColorUtil.lighten(base, 0.32 * hoverProgress);
        int pressedTint = ColorUtil.darken(base, 0.45 * pressProgress);
        int fill = ColorUtil.lerp(hoverTint, pressedTint, pressProgress);

        renderer.saveState();
        double centerX = bounds.getLeft() + bounds.getWidth() / 2.0;
        double centerY = bounds.getTop() + bounds.getHeight() / 2.0;
        renderer.translate(centerX, centerY, 0);
        renderer.scale(scale, scale, 1.0);
        renderer.translate(-centerX, -centerY, 0);

        SurfaceRendererExtensions.drawElevatedPanel(renderer, bounds, fill, cornerRadius, elevation, sheen);

        if (hoverProgress > 0.01 || selected) {
            double overlay = Math.min(0.65, 0.28 * hoverProgress + (selected ? 0.22 : 0.0) + rippleFade * 0.35);
            Rect glowBounds = bounds.inset(new Insets(2.0, 2.0, 2.0, 2.0));
            renderer.drawRect(glowBounds, ColorUtil.withOpacity(accentColor, overlay), Math.max(0.0, cornerRadius - 2.0));
        }

        if (rippleFade > 0.001) {
            Rect rippleBounds = bounds.inset(new Insets(4.0, 4.0, 4.0, 4.0));
            pressRipple
                    .color(ColorUtil.withOpacity(ColorUtil.lighten(accentColor, 0.25), 0.45 + rippleFade * 0.4))
                    .intensity(0.22 + rippleFade * 0.55 + pressProgress * 0.2)
                    .render(renderer, rippleBounds, rippleProgress);
        }

        renderer.restoreState();

        double labelOffset = pressProgress * 1.5;
        Vec2 labelPos = new Vec2(bounds.getLeft() + 16, bounds.getTop() + 10 + labelOffset);
        label.setPosition(labelPos);
        label.color(textColor);
        label.opacity(Math.max(0.35, 1.0 - pressProgress * 0.3));
        label.setVisible(true);
        label.render(context, renderer);
        label.setVisible(false);
    }

    public void click() {
        playPressFeedback();
        if (listener != null) {
            listener.onClick();
        }
    }

    private AnimationTrack<Double> createHoverTrack() {
        AnimationTrack<Double> track = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * Easings.easeOutExpo(t);
            }
        });
        track.addKeyframe(new AnimationKeyframe<Double>(0.0, 0.0));
        track.addKeyframe(new AnimationKeyframe<Double>(0.18, 1.0));
        return track;
    }

    private AnimationTrack<Double> createPressTrack() {
        AnimationTrack<Double> track = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                double eased = Easings.elasticOut(t);
                return start + (end - start) * eased;
            }
        });
        track.addKeyframe(new AnimationKeyframe<Double>(0.0, 0.0));
        track.addKeyframe(new AnimationKeyframe<Double>(0.12, 1.0));
        track.addKeyframe(new AnimationKeyframe<Double>(0.32, 0.0));
        return track;
    }

    private AnimationTrack<Double> createRippleProgressTrack() {
        AnimationTrack<Double> track = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * Easings.easeOutQuart(t);
            }
        });
        track.addKeyframe(new AnimationKeyframe<Double>(0.0, 0.0));
        track.addKeyframe(new AnimationKeyframe<Double>(0.2, 0.35));
        track.addKeyframe(new AnimationKeyframe<Double>(0.45, 1.0));
        return track;
    }

    private AnimationTrack<Double> createRippleFadeTrack() {
        AnimationTrack<Double> track = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * Easings.easeOutCubic(t);
            }
        });
        track.addKeyframe(new AnimationKeyframe<Double>(0.0, 1.0));
        track.addKeyframe(new AnimationKeyframe<Double>(0.28, 0.85));
        track.addKeyframe(new AnimationKeyframe<Double>(0.6, 0.0));
        return track;
    }
}
