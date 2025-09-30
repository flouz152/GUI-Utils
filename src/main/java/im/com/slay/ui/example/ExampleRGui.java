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
import im.com.slay.ui.core.UIListView;
import im.com.slay.ui.core.UIModal;
import im.com.slay.ui.core.UIPanel;
import im.com.slay.ui.core.UISlider;
import im.com.slay.ui.core.UIToggle;
import im.com.slay.ui.core.UIFlowContainer;
import im.com.slay.ui.geometry.Insets;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.layout.FlexLayout;
import im.com.slay.ui.layout.FlowLayout;
import im.com.slay.ui.layout.LayoutConstraints;
import im.com.slay.ui.state.StateHandle;
import im.com.slay.ui.theme.ThemeManager;
import im.com.slay.ui.theme.ThemePalette;
import im.com.slay.ui.theme.ThemeVariant;

import java.util.Arrays;
import java.util.List;

/**
 * Example GUI assembled using the toolkit. The screen mimics a modern command
 * palette and demonstrates dynamic state bindings and animations. Intended to
 * be opened via the R key binding.
 */
public final class ExampleRGui extends UIPanel {

    private final StateHandle<String> commandState = new StateHandle<String>("/warp spawn");
    private final AnimationTimeline sheenTimeline;
    private final PathInterpolator sheenEasing = new PathInterpolator(0.65, 0.0, 0.35, 1.0);
    private final UISlider sheenSlider = new UISlider().range(0.2, 0.8).value(0.4);
    private final UIToggle themeToggle = new UIToggle().state(false);
    private final UIListView<String> commandList = new UIListView<String>();
    private final UIModal helpModal = new UIModal();
    private final ThemeManager themeManager = new ThemeManager();

    public ExampleRGui() {
        super();
        setLayout(FlexLayout.vertical().gap(12));
        setPalette(ThemePalette.DEFAULT);
        elevation(10).cornerRadius(18).sheen(0.4);
        themeManager.register("default", ThemePalette.DEFAULT);
        themeManager.register("sunrise", ThemeVariant.lighten(ThemePalette.DEFAULT, 0.2));
        themeManager.register("midnight", ThemeVariant.darken(ThemePalette.DEFAULT, 0.35));
        themeManager.addListener(new ThemeManager.Listener() {
            @Override
            public void onThemeChanged(String name, ThemePalette palette) {
                setPalette(palette);
                for (UIComponent child : getChildren()) {
                    child.setPalette(palette);
                }
            }
        });
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
        configureModal();
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

        UIFlowContainer controls = new UIFlowContainer();
        controls.setLayout(FlowLayout.horizontal().gap(10));
        controls.setPalette(getPalette());
        controls.addChild(createCommandButton("Warp Spawn", "/warp spawn"));
        controls.addChild(createCommandButton("Warp Home", "/home"));
        controls.addChild(createCommandButton("Guild UI", "/guild ui"));
        controls.addChild(createCommandButton("Market", "/ah"));
        addChild(controls);

        sheenSlider.onChange(new UISlider.Listener() {
            @Override
            public void onValueChanged(double value) {
                sheen(value);
            }
        });
        addChild(sheenSlider);

        themeToggle.onToggle(new UIToggle.Listener() {
            @Override
            public void onToggled(boolean enabled) {
                themeManager.apply(enabled ? "sunrise" : "default");
            }
        });
        addChild(themeToggle);

        commandList.itemHeight(32);
        commandList.items(sampleCommands());
        commandList.renderer(new UIListView.CellRenderer<String>() {
            @Override
            public void render(String item, Rect bounds, im.com.slay.ui.render.SurfaceRenderer renderer,
                               UIListView.ThemeContext theme) {
                renderer.drawText(item, new Vec2(bounds.getLeft() + 12, bounds.getTop() + 10),
                        theme.palette().getTextPrimaryColor(), 14, 0.35);
            }
        });
        addChild(commandList);

        UIButton helpButton = new UIButton("Show Shortcuts");
        helpButton.onClick(new UIButton.ClickListener() {
            @Override
            public void onClick() {
                helpModal.show();
            }
        });
        addChild(helpButton);

        addChild(helpModal);
    }

    private List<String> sampleCommands() {
        return Arrays.asList(
                "/warp spawn",
                "/warp home",
                "/guild ui",
                "/market",
                "/skills",
                "/bounty list",
                "/toggle particles",
                "/is upgrade"
        );
    }

    private UIButton createCommandButton(final String label, final String command) {
        UIButton button = new UIButton(label);
        button.onClick(new UIButton.ClickListener() {
            @Override
            public void onClick() {
                commandState.set(command);
            }
        });
        return button;
    }

    private void configureModal() {
        helpModal.setVisible(false);
        UIPanel sheet = new UIPanel().cornerRadius(24).elevation(16).sheen(0.5);
        sheet.setLayout(FlexLayout.vertical().gap(8));
        sheet.setMargin(new Insets(120, 0, 0, 0));
        sheet.addChild(new UILabel("Keyboard Shortcuts").fontSize(20));
        sheet.addChild(new UILabel("R – Open palette").fontSize(14));
        sheet.addChild(new UILabel("Shift + R – Quick command").fontSize(14));
        sheet.addChild(new UILabel("Ctrl + F – Focus search").fontSize(14));
        UIButton dismiss = new UIButton("Close");
        dismiss.onClick(new UIButton.ClickListener() {
            @Override
            public void onClick() {
                helpModal.hide();
            }
        });
        sheet.addChild(dismiss);
        helpModal.addChild(sheet);
    }

    @Override
    public void attachToContext(UIContext context) {
        super.attachToContext(context);
        setPosition(new Vec2(120, 80));
        sheenTimeline.reset();
    }

    @Override
    protected void onChildAdded(UIComponent child) {
        if (child == commandList) {
            setConstraints(child, LayoutConstraints.none().grow(1));
        } else if (child == helpModal) {
            setConstraints(child, LayoutConstraints.none().grow(1));
        } else {
            setConstraints(child, LayoutConstraints.none());
        }
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
