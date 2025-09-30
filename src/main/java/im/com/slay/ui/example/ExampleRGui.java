package im.com.slay.ui.example;

import im.com.slay.ui.animation.AnimationKeyframe;
import im.com.slay.ui.animation.AnimationTimeline;
import im.com.slay.ui.animation.AnimationTrack;
import im.com.slay.ui.animation.PathInterpolator;
import im.com.slay.ui.animation.TimelineBuilder;
import im.com.slay.ui.core.UIButton;
import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContext;
import im.com.slay.ui.core.UILabel;
import im.com.slay.ui.core.UIPanel;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.layout.FlexLayout;
import im.com.slay.ui.state.StateHandle;
import im.com.slay.ui.theme.ThemePalette;

/**
 * Example GUI assembled using the toolkit. The screen mimics a modern command
 * palette and demonstrates dynamic state bindings and animations. Intended to
 * be opened via the R key binding.
 */
public final class ExampleRGui extends UIPanel {

    private final StateHandle<String> commandState = new StateHandle<String>("/warp spawn");
    private final AnimationTimeline sheenTimeline;
    private final PathInterpolator sheenEasing = new PathInterpolator(0.65, 0.0, 0.35, 1.0);

    public ExampleRGui() {
        super();
        setLayout(FlexLayout.vertical().gap(12));
        setPalette(ThemePalette.DEFAULT);
        elevation(10).cornerRadius(18).sheen(0.4);
        sheenTimeline = TimelineBuilder.timeline()
                .loop(true)
                .track("sheen", buildSheenTrack())
                .build();
        sheenTimeline.addListener(new AnimationTimeline.AnimationListener() {
            @Override
            public void onTick(AnimationTimeline timeline, double time) {
                Double value = timeline.sample("sheen", time);
                if (value != null) {
                    sheen(value.doubleValue());
                }
            }

            @Override
            public void onLoop(AnimationTimeline timeline) {
            }

            @Override
            public void onFinished(AnimationTimeline timeline) {
            }
        });
        addTimeline(sheenTimeline);
        configure();
    }

    private void configure() {
        UILabel title = new UILabel("Slay UI Command Palette").fontSize(22);
        addChild(title);

        final UILabel description = new UILabel("Selected: " + commandState.get());
        commandState.observe(new StateHandle.Observer<String>() {
            @Override
            public void onChanged(String value) {
                description.text("Selected: " + value);
            }
        });
        addChild(description);

        UIButton spawnButton = new UIButton("Warp Spawn");
        spawnButton.onClick(new UIButton.ClickListener() {
            @Override
            public void onClick() {
                commandState.set("/warp spawn");
            }
        });
        addChild(spawnButton);

        UIButton homeButton = new UIButton("Warp Home");
        homeButton.onClick(new UIButton.ClickListener() {
            @Override
            public void onClick() {
                commandState.set("/home");
            }
        });
        addChild(homeButton);

        UIButton guildButton = new UIButton("Open Guild UI");
        guildButton.onClick(new UIButton.ClickListener() {
            @Override
            public void onClick() {
                commandState.set("/guild ui");
            }
        });
        addChild(guildButton);
    }

    @Override
    public void attachToContext(UIContext context) {
        super.attachToContext(context);
        setPosition(new Vec2(120, 80));
        sheenTimeline.reset();
    }

    @Override
    protected void onChildAdded(UIComponent child) {
        setConstraints(child, im.com.slay.ui.layout.LayoutConstraints.none().grow(1));
    }

    private AnimationTrack<Double> buildSheenTrack() {
        AnimationTrack<Double> track = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                double eased = sheenEasing.interpolate(t);
                return start + (end - start) * eased;
            }
        });
        track.addKeyframe(new AnimationKeyframe<Double>(0.0, Double.valueOf(0.3)))
                .addKeyframe(new AnimationKeyframe<Double>(1.5, Double.valueOf(0.55)))
                .addKeyframe(new AnimationKeyframe<Double>(3.0, Double.valueOf(0.3)));
        return track;
    }
}
