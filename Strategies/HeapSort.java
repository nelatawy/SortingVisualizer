package Strategies;

import Metrics.SortingMetrics;
import Metrics.Steps.CompareStep;
import Metrics.Steps.SwapStep;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HeapSort<E extends Comparable<E>> extends SortingStrategy<E> {

    private void heapify(List<E> list, Comparator<E> comparator, int index, SortingMetrics<E> metrics) {
        int leftChild = index * 2;
        int rightChild = index * 2 + 1;

        int maxIdx = index;

        if (leftChild < list.size()) {
            super.addToMetrics(new CompareStep<>(leftChild, maxIdx), metrics);
            if(comparator.compare(list.get(leftChild), list.get(maxIdx)) > 0) {
                maxIdx = leftChild;
            }
        }
        if (rightChild < list.size()) {
            super.addToMetrics(new CompareStep<>(rightChild, maxIdx), metrics);
            if(comparator.compare(list.get(rightChild), list.get(maxIdx)) > 0) {
                maxIdx = rightChild;
            }
        }

        if (maxIdx != index) {
            Collections.swap(list, maxIdx, index);
            super.addToMetrics(new SwapStep<>(maxIdx, index), metrics);
            heapify(list, comparator, maxIdx, metrics);
        }
    }

    private void buildHeap(List<E> list, Comparator<E> comparator, SortingMetrics<E> metrics) {
        for (int i = list.size() / 2; i >= 0; i--) {
            heapify(list, comparator, i, metrics);
        }
    }

    @Override
    public List<E> sort(List<E> list, Comparator<E> comparator, SortingMetrics<E> metrics) {
        buildHeap(list, comparator,  metrics);

        for (int i = list.size() - 1; i >= 0; i--) {
            Collections.swap(list,0, i);
            super.addToMetrics(new SwapStep<>(0, i), metrics);
            heapify(list.subList(0,i), comparator, 0,  metrics);
        }
        return list;
    }


}
