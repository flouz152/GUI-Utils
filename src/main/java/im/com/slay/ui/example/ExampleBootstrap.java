package im.com.slay.ui.example;

import im.com.slay.ui.core.UIContext;
import im.com.slay.ui.events.UIEventBus;
import im.com.slay.ui.input.InputRouter;
import im.com.slay.ui.render.SurfaceRenderer;

/**
 * Wires together the toolkit pieces into a runnable demo. In an actual mod this
 * class would be instantiated during the Labymod addon initialization and the
 * {@link #tick(double)} method would be called from the render tick.
 */
public final class ExampleBootstrap {

    private static final int KEY_R = 19; // LWJGL key code for R

    private final UIContext context;
    private final ExampleRGui screen;
    private final SurfaceRenderer renderer;
    private boolean visible;

    public ExampleBootstrap(UIEventBus bus, SurfaceRenderer renderer) {
        this.renderer = renderer;
        this.context = new UIContext(bus, renderer);
        this.screen = new ExampleRGui();
        this.screen.attachToContext(context);
        InputRouter router = new InputRouter(bus);
        router.bind(KEY_R, new Runnable() {
            @Override
            public void run() {
                toggle();
            }
        });
    }

    public void toggle() {
        visible = !visible;
    }

    public void tick(double deltaTime) {
        if (!visible) {
            return;
        }
        screen.update(deltaTime);
        screen.layout(new im.com.slay.ui.geometry.Vec2(400, 300));
        screen.render(context, renderer);
    }

    public boolean isVisible() {
        return visible;
    }

    public ExampleRGui getScreen() {
        return screen;
    }
}
