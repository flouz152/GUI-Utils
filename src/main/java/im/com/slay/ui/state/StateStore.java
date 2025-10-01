package im.com.slay.ui.state;

import im.com.slay.ui.binding.Binding;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple state container reminiscent of Redux/Pinia stores. Provides structured
 * state, mutation helpers and computed derivations so that large GUI trees can
 * share data without tight coupling.
 */
public final class StateStore {

    public interface Mutator<T> {
        T mutate(T value);
    }

    private final Map<String, Object> state = new HashMap<String, Object>();
    private final Map<String, Binding<?>> bindings = new HashMap<String, Binding<?>>();

    public StateStore() {
    }

    public <T> void set(String key, T value) {
        state.put(key, value);
        notifyBinding(key, value);
    }

    public <T> T get(String key, T defaultValue) {
        Object value = state.get(key);
        if (value == null) {
            return defaultValue;
        }
        @SuppressWarnings("unchecked")
        T casted = (T) value;
        return casted;
    }

    public <T> Binding<T> binding(String key, T defaultValue) {
        @SuppressWarnings("unchecked")
        Binding<T> binding = (Binding<T>) bindings.get(key);
        if (binding == null) {
            binding = new Binding<T>(get(key, defaultValue));
            bindings.put(key, binding);
        }
        return binding;
    }

    public <T> void mutate(String key, Mutator<T> mutator, T defaultValue) {
        T current = get(key, defaultValue);
        T updated = mutator.mutate(current);
        set(key, updated);
    }

    private <T> void notifyBinding(String key, T value) {
        @SuppressWarnings("unchecked")
        Binding<T> binding = (Binding<T>) bindings.get(key);
        if (binding != null) {
            binding.set(value);
        }
    }
}
