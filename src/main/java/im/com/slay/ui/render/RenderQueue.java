package im.com.slay.ui.render;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Simple stage based render queue. Designed to be thread safe enough for light
 * usage inside the render loop while still allowing asynchronous preparation of
 * effects. The queue is intentionally compatible with labymod's render system
 * which expects deterministic ordering.
 */
public final class RenderQueue {

    private final Deque<RenderCommand> background = new ArrayDeque<RenderCommand>();
    private final Deque<RenderCommand> content = new ArrayDeque<RenderCommand>();
    private final Deque<RenderCommand> foreground = new ArrayDeque<RenderCommand>();
    private final Deque<RenderCommand> postProcess = new ArrayDeque<RenderCommand>();

    public void push(RenderCommand command) {
        switch (command.stage()) {
            case BACKGROUND:
                background.add(command);
                break;
            case CONTENT:
                content.add(command);
                break;
            case FOREGROUND:
                foreground.add(command);
                break;
            case POST_PROCESS:
                postProcess.add(command);
                break;
            default:
        }
    }

    public void flush() {
        drain(background);
        drain(content);
        drain(foreground);
        drain(postProcess);
    }

    private void drain(Deque<RenderCommand> queue) {
        while (!queue.isEmpty()) {
            RenderCommand command = queue.poll();
            if (command != null) {
                command.run();
            }
        }
    }
}
