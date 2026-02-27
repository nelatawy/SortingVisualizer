package Strategies;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HeapSort implements SortingStrategy {

    private <E extends Comparable<E>> void heapify(List<E> list, Comparator<E> comparator, int index) {
        int leftChild = index * 2;
        int rightChild = index * 2 + 1;

        int maxIdx = index;
        if (leftChild < list.size() && comparator.compare(list.get(leftChild), list.get(maxIdx)) > 0) {
            maxIdx = leftChild;
        }
        if (rightChild < list.size() && comparator.compare(list.get(rightChild), list.get(maxIdx)) > 0) {
            maxIdx = rightChild;
        }

        if (maxIdx != index) {
            Collections.swap(list, maxIdx, index);
            heapify(list, comparator, maxIdx);
        }
    }

    private <E extends Comparable<E>> void buildHeap(List<E> list, Comparator<E> comparator) {
        for (int i = list.size() / 2; i >= 0; i--) {
            heapify(list, comparator, i);
        }
    }

    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list, Comparator<E> comparator) {
        buildHeap(list, comparator);

        for (int i = list.size() - 1; i >= 0; i--) {
            Collections.swap(list,0, i);
            heapify(list.subList(0,i), comparator, 0);
        }
        return list;
    }

    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list) {
        return sort(list, Comparator.naturalOrder());
    }
}
