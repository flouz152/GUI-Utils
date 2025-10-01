package im.com.slay.ui.events;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Lightweight event bus. Supports subscription handles and is thread safe for
 * the limited concurrency needed in the modding environment.
 */
public final class UIEventBus {

    private final Map<Class<?>, CopyOnWriteArrayList<Listener<?>>> listeners = new ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Listener<?>>>();

    public <T extends UIEvent> Subscription subscribe(Class<T> type, Listener<T> listener) {
        CopyOnWriteArrayList<Listener<?>> list = listeners.get(type);
        if (list == null) {
            list = new CopyOnWriteArrayList<Listener<?>>();
            listeners.put(type, list);
        }
        list.add(listener);
        return new Subscription(type, listener);
    }

    public void publish(UIEvent event) {
        CopyOnWriteArrayList<Listener<?>> list = listeners.get(event.getClass());
        if (list == null) {
            return;
        }
        Iterator<Listener<?>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Listener listener = iterator.next();
            listener.onEvent(event);
        }
    }

    public final class Subscription {
        private final Class<?> type;
        private final Listener<?> listener;

        private Subscription(Class<?> type, Listener<?> listener) {
            this.type = type;
            this.listener = listener;
        }

        public void unsubscribe() {
            CopyOnWriteArrayList<Listener<?>> list = listeners.get(type);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    public interface Listener<T extends UIEvent> {
        void onEvent(T event);
    }
}
