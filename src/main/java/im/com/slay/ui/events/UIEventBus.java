package im.com.slay.ui.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Simple synchronous event bus for UI events.
 */
public final class UIEventBus<E> {

    private final List<Consumer<E>> listeners = new ArrayList<>();

    public void register(Consumer<E> listener) {
        listeners.add(listener);
    }

    public void unregister(Consumer<E> listener) {
        listeners.remove(listener);
    }

    public void publish(E event) {
        for (Consumer<E> listener : new ArrayList<>(listeners)) {
            listener.accept(event);
        }
    }
}
