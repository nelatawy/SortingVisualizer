package Strategies;

import Metrics.SortingMetrics;
import Metrics.Steps.CompareStep;
import Metrics.Steps.SetStep;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort<E extends Comparable<E>> extends SortingStrategy<E> {

    private List<E> merge(List<E> array1, List<E> array2, Comparator<E> comparator, SortingMetrics<E> metrics) {
        List<E> merged = new ArrayList<>();
        int itr1 = 0;
        int itr2 = 0;
        while (itr1 < array1.size() && itr2 < array2.size()) {

            super.addToMetrics(new CompareStep<>(itr1, itr2), metrics);
            if (comparator.compare(array1.get(itr1), array2.get(itr2)) < 0) {
                merged.add(array1.get(itr1));
                itr1++;
            }
            else {
                merged.add(array2.get(itr2));
                itr2++;
            }
        }
        while (itr1 < array1.size()) {
            merged.add(array1.get(itr1));
            itr1++;
        }
        while (itr2 < array2.size()) {
            merged.add(array2.get(itr2));
            itr2++;
        }
        return merged;
    }

    public List<E> sort(List<E> list, Comparator<E> comparator, SortingMetrics<E> metrics) {
        if (list == null || list.size() <= 1) return list;

        int mid = list.size() / 2;
        List<E> left = sort(list.subList(0, mid), comparator, metrics);
        List<E> right = sort(list.subList(mid, list.size()),  comparator, metrics);
        List<E> merged = merge(left, right, comparator, metrics);
        // instead of only returning the merged array we write it back in the original array before returning it
        // to mimic the behavior of sorting in place and have all the changes reflected in the original array

        for (int i = 0; i < merged.size(); i++) {
            list.set(i, merged.get(i));
            super.addToMetrics(new SetStep<>(i, merged.get(i)), metrics);
        }
        return list;
    }

}
