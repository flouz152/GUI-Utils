package im.com.slay.ui.state;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Reactive state container inspired by Vue's ref and React's useState. Supports
 * synchronous observers which makes it ideal for immediate mode UI updates.
 */
public final class StateHandle<T> {

    private final List<Observer<T>> observers = new CopyOnWriteArrayList<Observer<T>>();
    private T value;

    public StateHandle(T initialValue) {
        this.value = initialValue;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
        notifyObservers();
    }

    public void observe(Observer<T> observer) {
        observers.add(observer);
        observer.onChanged(value);
    }

    public void remove(Observer<T> observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer<T> observer : observers) {
            observer.onChanged(value);
        }
    }

    public interface Observer<T> {
        void onChanged(T value);
    }
}
