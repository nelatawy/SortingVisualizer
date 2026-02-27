package Strategies;

import Metrics.SortingMetrics;
import Metrics.Steps.CompareStep;
import Metrics.Steps.SwapStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectionSort<E extends Comparable<E>> extends SortingStrategy<E> {

    public List<E> sort(List<E> list, Comparator<E> comparator, SortingMetrics<E> metrics) {

        for (int i = 0; i < list.size(); i++) {
            int bestIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                super.addToMetrics(new CompareStep<>(j, bestIndex), metrics);
                if (comparator.compare(list.get(j), list.get(bestIndex)) < 0) {
                    bestIndex = j;
                }
            }
            if (bestIndex != i) {
                Collections.swap(list, bestIndex, i);
                super.addToMetrics(new SwapStep<>(bestIndex, i), metrics);
            }
        }
        return list;
    }


}
