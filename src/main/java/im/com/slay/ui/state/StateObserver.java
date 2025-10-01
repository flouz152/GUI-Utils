package im.com.slay.ui.state;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Observes two state suppliers and notifies the consumer whenever either of them changes.
 */
public final class StateObserver<A, B> {

    private final Supplier<A> first;
    private final Supplier<B> second;
    private final BiConsumer<A, B> listener;
    private A previousA;
    private B previousB;

    public StateObserver(Supplier<A> first, Supplier<B> second, BiConsumer<A, B> listener) {
        this.first = first;
        this.second = second;
        this.listener = listener;
        this.previousA = first.get();
        this.previousB = second.get();
    }

    public void update() {
        A currentA = first.get();
        B currentB = second.get();
        if (!Objects.equals(currentA, previousA) || !Objects.equals(currentB, previousB)) {
            previousA = currentA;
            previousB = currentB;
            listener.accept(currentA, currentB);
        }
    }
}
