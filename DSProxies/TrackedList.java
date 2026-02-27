package DSProxies;

import Metrics.SortingMetrics;
import Metrics.Steps.SetStep;

import java.util.*;


// and ArrayList implements List interface so this in turn also implements the List interface
public class TrackedList<E> extends ArrayList<E> {

    private final SortingMetrics<E> trackingMetrics;

    public TrackedList(List<E> list, SortingMetrics<E> metrics) {
        super(list);
        this.trackingMetrics = metrics;
    }

    public TrackedList(List<E> list) {
        super(list);
        trackingMetrics = null;
    }

    @Override
    public E set(int idx, E val){
        if (trackingMetrics != null) {
            trackingMetrics.addStep(new SetStep<>(idx, val)); //notifying the metrics
        }
        return super.set(idx, val);
    }

}
