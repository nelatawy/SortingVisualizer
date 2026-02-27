package Strategies;

import Metrics.SortingMetrics;
import Metrics.Steps.SortingStep;

import java.util.Comparator;
import java.util.List;

public abstract class SortingStrategy<E extends Comparable<E>>{

    void addToMetrics(SortingStep<E> step, SortingMetrics<E> metrics) {
        if (metrics != null){
            metrics.addStep(step);
        }
    }
    public abstract List<E> sort(List<E> list, Comparator<E> comparator, SortingMetrics<E> metrics);

    public List<E> sort(List<E> list, Comparator<E> comparator){
        return sort(list, comparator, null);
    }

    public List<E> sort(List<E> list){
        return sort(list, Comparator.naturalOrder());
    }
}
