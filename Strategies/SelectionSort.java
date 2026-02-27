package Strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectionSort implements SortingStrategy {
    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list, Comparator<E> comparator) {

        for (int i = 0; i < list.size(); i++) {
            int bestIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (comparator.compare(list.get(j), list.get(bestIndex)) < 0) {
                    bestIndex = j;
                }
            }
            if (bestIndex != i) {
                Collections.swap(list, bestIndex, i);
            }
        }
        return list;
    }

    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list) {
        return sort(list, Comparator.naturalOrder());
    }
}
