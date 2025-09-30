package im.com.slay.ui.core;

import im.com.slay.ui.events.UIEventBus;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.theme.ThemePalette;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Represents a runtime context in which components live. The context acts as
 * glue between the Labymod rendering API, the input subsystems and the
 * animation scheduler. Advanced consumers may create multiple contexts in
 * parallel to render into layered surfaces like in-game overlays or VR
 * displays.
 */
public final class UIContext {

    private final UIEventBus eventBus;
    private final SurfaceRenderer renderer;
    private final Executor asyncExecutor;
    private ThemePalette palette = ThemePalette.DEFAULT;

    public UIContext(UIEventBus eventBus, SurfaceRenderer renderer) {
        this(eventBus, renderer, Executors.newSingleThreadExecutor());
    }

    public UIContext(UIEventBus eventBus, SurfaceRenderer renderer, Executor asyncExecutor) {
        this.eventBus = eventBus;
        this.renderer = renderer;
        this.asyncExecutor = asyncExecutor;
    }

    public UIEventBus getEventBus() {
        return eventBus;
    }

    public SurfaceRenderer getRenderer() {
        return renderer;
    }

    public Executor getAsyncExecutor() {
        return asyncExecutor;
    }

    public ThemePalette getPalette() {
        return palette;
    }

    public void setPalette(ThemePalette palette) {
        this.palette = palette;
    }
}
