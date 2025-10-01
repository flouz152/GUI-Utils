package im.com.slay.ui.input;

import im.com.slay.ui.events.KeyEvent;
import im.com.slay.ui.events.UIEventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Central hub for dispatching input events to focused components. Works with
 * the event bus to make key binding configuration straightforward.
 */
public final class InputRouter {

    private final UIEventBus eventBus;
    private final Map<Integer, Runnable> keyBindings = new HashMap<Integer, Runnable>();

    public InputRouter(UIEventBus eventBus) {
        this.eventBus = eventBus;
        registerDefaultBindings();
    }

    private void registerDefaultBindings() {
        eventBus.subscribe(KeyEvent.class, new UIEventBus.Listener<KeyEvent>() {
            @Override
            public void onEvent(KeyEvent event) {
                if (event.getType() == KeyEvent.Type.PRESS) {
                    Runnable binding = keyBindings.get(event.getKeyCode());
                    if (binding != null) {
                        binding.run();
                    }
                }
            }
        });
    }

    public void bind(int keyCode, Runnable runnable) {
        keyBindings.put(keyCode, runnable);
    }

    public void unbind(int keyCode) {
        keyBindings.remove(keyCode);
    }
}
