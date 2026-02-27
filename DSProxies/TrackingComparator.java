package DSProxies;

import Metrics.SortingMetrics;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class TrackingComparator<E extends Comparable<E>> implements Comparator<E> {
    private final Comparator<E> comparator;
    private final SortingMetrics<E> metrics;

    public TrackingComparator(Comparator<E> comparator) {
        this.comparator = comparator;
        this.metrics = null;
    }

    public TrackingComparator(Comparator<E> comparator, SortingMetrics<E> metrics) {
        this.comparator = comparator;
        this.metrics = metrics;
    }

    @Override
    public int compare(E o1, E o2) {
//        metrics.addStep(new CompareStep<>());
        return this.comparator.compare(o1, o2);
    }

    @Override
    public Comparator<E> reversed() {
        return Comparator.super.reversed();
    }

    @Override
    public Comparator<E> thenComparing(Comparator<? super E> other) {
        return new TrackingComparator<>(comparator.thenComparing(other));
    }

    @Override
    public <U> Comparator<E> thenComparing(Function<? super E, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return new TrackingComparator<>(comparator.thenComparing(keyExtractor, keyComparator));
    }

    @Override
    public <U extends Comparable<? super U>> Comparator<E> thenComparing(Function<? super E, ? extends U> keyExtractor) {
        return new TrackingComparator<>(comparator.thenComparing(keyExtractor));
    }

    @Override
    public Comparator<E> thenComparingInt(ToIntFunction<? super E> keyExtractor) {
        return new TrackingComparator<>(comparator.thenComparingInt(keyExtractor));
    }

    @Override
    public Comparator<E> thenComparingLong(ToLongFunction<? super E> keyExtractor) {
        return new TrackingComparator<>(comparator.thenComparingLong(keyExtractor));
    }

    @Override
    public Comparator<E> thenComparingDouble(ToDoubleFunction<? super E> keyExtractor) {
        return new TrackingComparator<>(comparator.thenComparingDouble(keyExtractor));
    }
}
