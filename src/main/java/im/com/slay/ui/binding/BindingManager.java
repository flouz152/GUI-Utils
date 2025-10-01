package im.com.slay.ui.binding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Central registry that keeps track of bindings and derived computed values.
 * The manager supports scoped lifetimes and bulk disposal, similar to how Vue
 * manages reactive effects. Useful for GUIs that want to clear listeners when
 * unmounted.
 */
public final class BindingManager {

    private final Map<String, Binding<?>> bindings = new HashMap<String, Binding<?>>();

    public BindingManager() {
    }

    /**
     * Registers a binding with a developer-defined key. If a binding already
     * exists at that key it will be replaced.
     */
    public <T> void register(String key, Binding<T> binding) {
        bindings.put(key, binding);
    }

    @SuppressWarnings("unchecked")
    public <T> Binding<T> get(String key) {
        return (Binding<T>) bindings.get(key);
    }

    /**
     * Removes a binding from the registry, returning it to the caller so they
     * can further operate on it if desired.
     */
    public Binding<?> remove(String key) {
        return bindings.remove(key);
    }

    /**
     * Merges bindings from another manager instance. Existing keys will be
     * preserved unless {@code overwriteExisting} is true.
     */
    public void merge(BindingManager other, boolean overwriteExisting) {
        Iterator<Map.Entry<String, Binding<?>>> iterator = other.bindings.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Binding<?>> entry = iterator.next();
            if (overwriteExisting || !bindings.containsKey(entry.getKey())) {
                bindings.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Clears all bindings.
     */
    public void clear() {
        bindings.clear();
    }
}
