package Strategies;

import Metrics.SortingMetrics;
import Metrics.Steps.CompareStep;
import Metrics.Steps.SwapStep;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuickSort<E extends Comparable<E>> extends SortingStrategy<E>{

    private int partition(List<E> list, int start, int end,
                          Comparator<E> comparator, SortingMetrics<E> metrics) {
        E pivot = list.get(end - 1);
        int lastSE = start - 1; //index to the last element that is not larger than the pivot
        int itr = start;
        while(itr < end - 1) {
            super.addToMetrics(new CompareStep<>(itr, end - 1), metrics);
            if(comparator.compare(list.get(itr),pivot) <= 0){
                lastSE += 1;
                Collections.swap(list, itr, lastSE);
                super.addToMetrics(new SwapStep<>(itr, lastSE), metrics);
            }
            itr += 1;
        }

        Collections.swap(list, lastSE + 1, end - 1); //position the pivot in its correct spot
        super.addToMetrics(new SwapStep<>(lastSE + 1, end - 1), metrics);
        return lastSE + 1;
    }

    /*
    This method exploits the principle of randomized algorithms to avoid worst-case scenarios
    and get the O(n.lgn) average running time even in Sorted or Reversely Sorted situations
     */
    private int randomizedPartition(List<E> list, int start, int end,
                                    Comparator<E> comparator, SortingMetrics<E> metrics) {
        int swapIdx = (int)(Math.random()*((end - 1) - start)) + start;
        Collections.swap(list, swapIdx, end - 1);
        super.addToMetrics(new SwapStep<>(swapIdx, list.size() - 1), metrics);
        return partition(list, start, end, comparator, metrics);
    }


    public List<E> sort(List<E> list, Comparator<E> comparator, SortingMetrics<E> metrics) {
        quickSort(list, 0, list.size(), comparator, metrics);
        return list;
    }

    private void quickSort(List<E> list, int start, int end,
                           Comparator<E> comparator,  SortingMetrics<E> metrics) {
        if (list == null || start >= end - 1) {
            return;
        }
        int pivot_idx = partition(list,start,end, comparator,  metrics);
        quickSort(list, start, pivot_idx, comparator, metrics);
        quickSort(list, pivot_idx + 1, end, comparator, metrics);
        // since we are passing a sublist then we are passing a view to the same array
        // and the partition method operates on the original array itself

        // so it utilizes the sorting-in-place feature of QuickSort and we simply
    }


}
