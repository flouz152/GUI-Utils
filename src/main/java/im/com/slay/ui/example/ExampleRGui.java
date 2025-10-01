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
import im.com.slay.ui.effects.RippleEffect;
import im.com.slay.ui.geometry.Insets;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.layout.FlexLayout;
import im.com.slay.ui.layout.FlowLayout;
import im.com.slay.ui.layout.LayoutConstraints;
import im.com.slay.ui.state.StateHandle;
import im.com.slay.ui.render.GradientBrush;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.render.SurfaceRendererExtensions;
import im.com.slay.ui.theme.ColorUtil;
import im.com.slay.ui.theme.ThemeManager;
import im.com.slay.ui.theme.ThemePalette;
import im.com.slay.ui.theme.ThemeVariant;

import java.util.ArrayList;
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
    private final UIPanel helpSheet = new UIPanel();
    private final ThemeManager themeManager = new ThemeManager();
    private final RippleEffect rippleEffect = new RippleEffect().rings(4).thickness(22.0).intensity(0.4);
    private final AnimationTimeline ambientTimeline;
    private GradientBrush auroraBrush = new GradientBrush();
    private GradientBrush borderBrush = new GradientBrush();
    private double rippleProgress = 0.0;
    private double auroraProgress = 0.0;
    private final List<UIButton> commandButtons = new ArrayList<UIButton>();

    public ExampleRGui() {
        super();
        setLayout(FlexLayout.vertical().gap(12));
        setPalette(ThemePalette.DEFAULT);
        elevation(12).cornerRadius(22).sheen(0.48).glass(0.55).accentColor(getPalette().getAccentColor()).glow(getPalette().getAccentColor(), 0.55, 58.0);
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
                updateVisuals(palette);
                refreshCommandButtonStates(commandState.get());
            }
        });
        updateVisuals(getPalette());
        sheenTimeline = TimelineBuilder.timeline()
                .pingPong(true)
                .playbackSpeed(0.9)
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
        ambientTimeline = TimelineBuilder.timeline()
                .pingPong(true)
                .playbackSpeed(0.8)
                .track("ripple", buildLoopTrack(4.5))
                .track("aurora", buildLoopTrack(7.0))
                .build();
        ambientTimeline.addListener(new AnimationTimeline.AnimationListener() {
            @Override
            public void onTick(AnimationTimeline timeline, double time) {
                Double ripple = timeline.sample("ripple", time);
                if (ripple != null) {
                    rippleProgress = ripple.doubleValue();
                }
                Double aurora = timeline.sample("aurora", time);
                if (aurora != null) {
                    auroraProgress = aurora.doubleValue();
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
        addTimeline(ambientTimeline);
        configure();
        configureModal();
    }

    private void updateVisuals(ThemePalette palette) {
        auroraBrush = new GradientBrush()
                .addStop(0.0, ColorUtil.withOpacity(ColorUtil.lighten(palette.getAccentColor(), 0.35), 0.22))
                .addStop(0.4, ColorUtil.withOpacity(ColorUtil.blend(palette.getPrimaryColor(), palette.getAccentColor(), 0.55), 0.18))
                .addStop(1.0, ColorUtil.withOpacity(ColorUtil.lighten(palette.getPrimaryVariantColor(), 0.2), 0.12));
        borderBrush = new GradientBrush()
                .addStop(0.0, ColorUtil.withOpacity(ColorUtil.lighten(palette.getPrimaryVariantColor(), 0.25), 0.4))
                .addStop(1.0, ColorUtil.withOpacity(ColorUtil.darken(palette.getPrimaryColor(), 0.15), 0.35));
        accentColor(palette.getAccentColor());
        glow(ColorUtil.withOpacity(palette.getAccentColor(), 0.65), Math.max(0.35, getGlowIntensity()), Math.max(48.0, getGlowSpread()));
        rippleEffect.color(ColorUtil.withOpacity(ColorUtil.lighten(palette.getAccentColor(), 0.25), 0.45));
        helpSheet.setPalette(palette);
        helpSheet.accentColor(palette.getAccentColor())
                .glass(0.5)
                .glow(ColorUtil.withOpacity(palette.getAccentColor(), 0.45), 0.4, 32.0)
                .backgroundColor(ColorUtil.blend(palette.getSurfaceColor(), palette.getPrimaryColor(), 0.28));
        for (UIButton button : commandButtons) {
            styleCommandButton(button, palette);
        }
    }

    private void configure() {
        commandButtons.clear();
        UILabel title = new UILabel("Slay UI Command Palette").fontSize(22);
        addChild(title);

        final UILabel description = new UILabel("Selected: " + commandState.get());
        commandState.observe(new StateHandle.Observer<String>() {
            @Override
            public void onChanged(String value) {
                description.text("Selected: " + value);
                refreshCommandButtonStates(value);
            }
        });
        addChild(description);

        UIFlowContainer controls = new UIFlowContainer();
        controls.setLayout(FlowLayout.horizontal().gap(10));
        controls.setPalette(getPalette());
        controls.addChild(registerCommandButton(createCommandButton("Warp Spawn", "/warp spawn")));
        controls.addChild(registerCommandButton(createCommandButton("Warp Home", "/home")));
        controls.addChild(registerCommandButton(createCommandButton("Guild UI", "/guild ui")));
        controls.addChild(registerCommandButton(createCommandButton("Market", "/ah")));
        addChild(controls);

        sheenSlider.onChange(new UISlider.Listener() {
            @Override
            public void onValueChanged(double value) {
                sheen(value);
                glass(Math.min(1.0, value + 0.35));
                glow(getGlowColor(), Math.max(0.25, value), Math.max(42.0, getGlowSpread()));
                rippleEffect.intensity(0.3 + value * 0.45);
            }
        });
        addChild(sheenSlider);
        rippleEffect.intensity(0.3 + sheenSlider.getValue() * 0.45);

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
                if (commandState.get().equals(item)) {
                    GradientBrush highlight = new GradientBrush()
                            .addStop(0.0, ColorUtil.withOpacity(theme.palette().getAccentColor(), 0.36))
                            .addStop(1.0, ColorUtil.withOpacity(ColorUtil.lighten(theme.palette().getAccentColor(), 0.3), 0.16));
                    Rect highlightRect = bounds.inset(new Insets(4, 10, 4, 10));
                    SurfaceRendererExtensions.drawGradientFill(renderer, highlightRect, highlight, GradientBrush.Orientation.HORIZONTAL, 12);
                    SurfaceRendererExtensions.drawGradientStroke(renderer, highlightRect, highlight, 1.5, 10);
                }
                renderer.drawText(item, new Vec2(bounds.getLeft() + 12, bounds.getTop() + 10),
                        theme.palette().getTextPrimaryColor(), 14, 0.35);
            }
        });
        addChild(commandList);

        UIButton helpButton = new UIButton("Show Shortcuts");
        styleCommandButton(helpButton, getPalette());
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
        final UIButton button = new UIButton(label);
        button.id(command);
        styleCommandButton(button, getPalette());
        boolean selected = command.equals(commandState.get());
        button.setSelected(selected);
        button.setHovered(selected);
        button.onClick(new UIButton.ClickListener() {
            @Override
            public void onClick() {
                commandState.set(command);
                ambientTimeline.reset();
            }
        });
        return button;
    }

    private UIButton registerCommandButton(UIButton button) {
        commandButtons.add(button);
        return button;
    }

    private void refreshCommandButtonStates(String selectedCommand) {
        for (UIButton button : commandButtons) {
            boolean matches = selectedCommand != null && selectedCommand.equals(button.getId());
            button.setSelected(matches);
            button.setHovered(matches);
            if (!matches) {
                button.setPressed(false);
            }
        }
    }

    private void styleCommandButton(UIButton button, ThemePalette palette) {
        int baseFill = ColorUtil.blend(palette.getSurfaceColor(), palette.getPrimaryColor(), 0.35);
        button.backgroundColor(ColorUtil.withOpacity(baseFill, 0.95))
                .accentColor(palette.getAccentColor())
                .textColor(palette.getTextPrimaryColor())
                .cornerRadius(16);
    }

    private void configureModal() {
        helpModal.setVisible(false);
        helpSheet.cornerRadius(28).elevation(18).sheen(0.55);
        helpSheet.setLayout(FlexLayout.vertical().gap(8));
        helpSheet.setMargin(new Insets(120, 0, 0, 0));
        helpSheet.addChild(new UILabel("Keyboard Shortcuts").fontSize(20));
        helpSheet.addChild(new UILabel("R – Open palette").fontSize(14));
        helpSheet.addChild(new UILabel("Shift + R – Quick command").fontSize(14));
        helpSheet.addChild(new UILabel("Ctrl + F – Focus search").fontSize(14));
        UIButton dismiss = new UIButton("Close");
        styleCommandButton(dismiss, getPalette());
        dismiss.onClick(new UIButton.ClickListener() {
            @Override
            public void onClick() {
                helpModal.hide();
            }
        });
        helpSheet.addChild(dismiss);
        helpModal.addChild(helpSheet);
        refreshCommandButtonStates(commandState.get());
    }

    @Override
    public void attachToContext(UIContext context) {
        super.attachToContext(context);
        setPosition(new Vec2(120, 80));
        sheenTimeline.reset();
        ambientTimeline.reset();
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

    private AnimationTrack<Double> buildLoopTrack(final double duration) {
        AnimationTrack<Double> track = new AnimationTrack<Double>(new AnimationTrack.Interpolator<Double>() {
            @Override
            public Double interpolate(Double start, Double end, double t) {
                return start + (end - start) * t;
            }
        });
        track.addKeyframe(new AnimationKeyframe<Double>(0.0, Double.valueOf(0.0)))
                .addKeyframe(new AnimationKeyframe<Double>(duration, Double.valueOf(1.0)));
        return track;
    }

    @Override
    protected void onRender(UIContext context, SurfaceRenderer renderer, Rect bounds) {
        super.onRender(context, renderer, bounds);
        Rect inner = bounds.inset(new Insets(18, 18, 18, 18));
        SurfaceRendererExtensions.drawAurora(renderer, inner, auroraBrush, auroraProgress, Math.max(0.0, getCornerRadius() - 12.0));
        SurfaceRendererExtensions.drawGradientFill(renderer, inner, borderBrush, GradientBrush.Orientation.VERTICAL, Math.max(0.0, getCornerRadius() - 14.0));
        SurfaceRendererExtensions.drawGradientStroke(renderer, inner.expand(6.0), borderBrush, 3.0, getCornerRadius() + 6.0);
        rippleEffect.render(renderer, inner, rippleProgress);
    }
}
