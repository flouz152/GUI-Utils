package im.com.slay.ui.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Lightweight observable list that dispatches granular change events. Inspired
 * by LiveData collections, allowing UI components to react to inserts, removes
 * and updates individually.
 */
public final class ObservableList<T> implements Iterable<T> {

    public interface Listener<T> {
        void onInserted(int index, T value);

        void onRemoved(int index, T value);

        void onChanged(int index, T value);

        void onCleared();
    }

    private final List<T> backing = new ArrayList<T>();
    private final List<Listener<T>> listeners = new ArrayList<Listener<T>>();

    public ObservableList() {
    }

    public ObservableList(Collection<T> items) {
        backing.addAll(items);
    }

    public void addListener(Listener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener<T> listener) {
        listeners.remove(listener);
    }

    public void add(T item) {
        backing.add(item);
        int index = backing.size() - 1;
        notifyInserted(index, item);
    }

    public void add(int index, T item) {
        backing.add(index, item);
        notifyInserted(index, item);
    }

    public T remove(int index) {
        T removed = backing.remove(index);
        notifyRemoved(index, removed);
        return removed;
    }

    public void clear() {
        backing.clear();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onCleared();
        }
    }

    public void set(int index, T item) {
        backing.set(index, item);
        notifyChanged(index, item);
    }

    public T get(int index) {
        return backing.get(index);
    }

    public int size() {
        return backing.size();
    }

    private void notifyInserted(int index, T item) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onInserted(index, item);
        }
    }

    private void notifyRemoved(int index, T item) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onRemoved(index, item);
        }
    }

    private void notifyChanged(int index, T item) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onChanged(index, item);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return backing.iterator();
    }

    public List<T> snapshot() {
        return new ArrayList<T>(backing);
    }
}
