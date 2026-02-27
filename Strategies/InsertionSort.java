package Strategies;

import Metrics.SortingMetrics;
import Metrics.Steps.CompareStep;
import Metrics.Steps.SetStep;
import Metrics.Steps.SwapStep;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InsertionSort<E extends Comparable<E>> extends SortingStrategy<E> {

    public List<E> sort(List<E> list, Comparator<E> comparator, SortingMetrics<E> metrics) {
        for (int i = 1; i < list.size(); i++) {
            E pivot = list.get(i);
            int j = i - 1;
            super.addToMetrics(new CompareStep<>(i, j), metrics); // initial comparison
            while (j >=0 && comparator.compare(list.get(j), pivot) > 0) {
                list.set(j+1, list.get(j));
                super.addToMetrics(new CompareStep<>(i, j), metrics);
                super.addToMetrics(new SwapStep<>(j, j + 1), metrics);
                j--;
                // we technically didn't swap but the behavior is the same
            }
            if (j != i - 1) {
                list.set(j+1, pivot);
                super.addToMetrics(new SetStep<>(j + 1, pivot), metrics);
            }
        }
        return list;
    }

}
