package Strategies;

import Metrics.SortingMetrics;
import Metrics.Steps.CompareStep;
import Metrics.Steps.SortingStep;
import Metrics.Steps.SwapStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BubbleSort<E extends Comparable<E>> extends SortingStrategy<E>{

    public List<E> sort(List<E> list, Comparator<E> comparator, SortingMetrics<E> metrics) {
        for (int i = 0; i < list.size(); i++){
            for (int j = 0; j < list.size() - 1; j++){
                super.addToMetrics(new CompareStep<>(j, j + 1), metrics);
                if (comparator.compare(list.get(j + 1),list.get(j)) < 0){
                    Collections.swap(list, j, j + 1);
                    super.addToMetrics(new SwapStep<>(j, j + 1), metrics);
                }
            }
        }
        return list;
    }
}
