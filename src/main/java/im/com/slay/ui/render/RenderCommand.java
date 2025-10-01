package im.com.slay.ui.render;

/**
 * Describes a deferred render operation. Commands make it possible to record
 * animations and complex effects on background threads and replay them during
 * the render tick to prevent frame drops.
 */
public interface RenderCommand extends Runnable {
    RenderStage stage();

    enum RenderStage {
        BACKGROUND,
        CONTENT,
        FOREGROUND,
        POST_PROCESS
    }
}
