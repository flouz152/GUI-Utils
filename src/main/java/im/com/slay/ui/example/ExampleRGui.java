package im.com.slay.ui.example;

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

    public ExampleRGui() {
        super();
        setLayout(FlexLayout.vertical().gap(12));
        setPalette(ThemePalette.DEFAULT);
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
    }

    @Override
    protected void onChildAdded(UIComponent child) {
        setConstraints(child, im.com.slay.ui.layout.LayoutConstraints.none().grow(1));
    }
}
