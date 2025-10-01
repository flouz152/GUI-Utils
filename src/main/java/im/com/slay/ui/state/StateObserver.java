package im.com.slay.ui.state;

/**
 * Helper to bind multiple state handles into a composite transformation. Each
 * observer can calculate derived values and push updates to UI components.
 */
public abstract class StateObserver<A, B> {

    private final StateHandle<A> first;
    private final StateHandle<B> second;

    public StateObserver(StateHandle<A> first, StateHandle<B> second) {
        this.first = first;
        this.second = second;
        hook();
    }

    private void hook() {
        first.observe(new StateHandle.Observer<A>() {
            @Override
            public void onChanged(A value) {
                onChanged(value, second.get());
            }
        });
        second.observe(new StateHandle.Observer<B>() {
            @Override
            public void onChanged(B value) {
                onChanged(first.get(), value);
            }
        });
    }

    protected abstract void onChanged(A first, B second);
}
