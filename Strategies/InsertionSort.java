package Strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InsertionSort implements SortingStrategy {
    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list, Comparator<E> comparator) {
        for (int i = 1; i < list.size(); i++) {
            E pivot = list.get(i);
            int j = i - 1;
            while (j >=0 && comparator.compare(list.get(j), pivot) > 0) {
                list.set(j+1, list.get(j));
                j--;
            }
            if (j != i - 1) {
                list.set(j+1, pivot);
            }
        }
        return list;
    }

    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list) {
        return sort(list, Comparator.naturalOrder());
    }
}
