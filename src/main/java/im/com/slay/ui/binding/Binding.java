package im.com.slay.ui.binding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a unidirectional value binding between a data source and one or
 * more subscribers. Bindings can be chained together to derive computed values
 * or listen for changes. Designed to work similarly to hooks in modern UI
 * frameworks, but expressed using plain Java interfaces for compatibility with
 * Labymod.
 */
public class Binding<T> {

    /**
     * Listener interface notified whenever the binding value changes.
     */
    public interface Listener<T> {
        void onChanged(T value);
    }

    private final List<Listener<T>> listeners = new ArrayList<Listener<T>>();
    private T value;

    public Binding() {
    }

    public Binding(T initialValue) {
        this.value = initialValue;
    }

    /**
     * Updates the current value and notifies all listeners. The update happens
     * synchronously and preserves insertion order for listeners so predictable
     * cascades can be built.
     */
    public void set(T value) {
        if (value == this.value || (value != null && value.equals(this.value))) {
            return;
        }
        this.value = value;
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onChanged(value);
        }
    }

    /**
     * Returns the current value without triggering re-computation.
     */
    public T get() {
        return value;
    }

    /**
     * Adds a listener to the binding. Returns the binding to enable fluent
     * configuration.
     */
    public Binding<T> listen(Listener<T> listener) {
        listeners.add(listener);
        if (value != null) {
            listener.onChanged(value);
        }
        return this;
    }

    /**
     * Removes a previously registered listener.
     */
    public void remove(Listener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * Creates a new binding derived from this binding using a transformer
     * function.
     */
    public <R> Binding<R> map(final Transformer<T, R> transformer) {
        final Binding<R> derived = new Binding<R>();
        listen(new Listener<T>() {
            @Override
            public void onChanged(T value) {
                derived.set(transformer.transform(value));
            }
        });
        if (value != null) {
            derived.set(transformer.transform(value));
        }
        return derived;
    }

    public interface Transformer<I, O> {
        O transform(I input);
    }

    /**
     * Creates a read-only facade for the binding. This is useful to expose
     * state handles to consumers without giving write access, mimicking the
     * separation between state and dispatch functions in React.
     */
    public Binding<T> readonly() {
        final Binding<T> parent = this;
        return new Binding<T>() {
            @Override
            public void set(T value) {
                throw new UnsupportedOperationException("Read only binding");
            }

            @Override
            public T get() {
                return parent.get();
            }

            @Override
            public Binding<T> listen(Listener<T> listener) {
                parent.listen(listener);
                if (parent.get() != null) {
                    listener.onChanged(parent.get());
                }
                return this;
            }

            @Override
            public void remove(Listener<T> listener) {
                parent.remove(listener);
            }
        };
    }

    /**
     * Returns an unmodifiable snapshot of the registered listeners. Intended
     * for diagnostics and testing.
     */
    public List<Listener<T>> getListeners() {
        return Collections.unmodifiableList(listeners);
    }
}
