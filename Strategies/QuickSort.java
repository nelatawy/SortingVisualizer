package Strategies;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuickSort implements SortingStrategy{

    private <E extends Comparable<E>> int partition(List<E> list, Comparator<E> comparator) {
        E pivot = list.getLast();
        int lastSE = -1; //index to the last element that is not larger than the pivot
        int itr = 0;
        while(itr < list.size() - 1) {
           if(comparator.compare(list.get(itr),pivot) <= 0){
               lastSE += 1;
               Collections.swap(list, itr, lastSE);
           }
           itr += 1;
        }
        Collections.swap(list, lastSE + 1, list.size() - 1); //position the pivot in it's correct spot
        return lastSE + 1;
    }

    /*
    This method exploits the principle of randomized algorithms to avoid worst-case scenarios
    and get the O(n.lgn) average running time even in Sorted or Reversely Sorted situations
     */
    private <E extends Comparable<E>> int randomizedPartition(List<E> list, Comparator<E> comparator) {
        int swapIdx = (int)(Math.random()*(list.size() - 1));
        Collections.swap(list, swapIdx, list.size() - 1);
        return partition(list, comparator);
    }

    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list, Comparator<E> comparator) {
        if (list == null || list.isEmpty() || list.size() <= 1) {
            return list;
        }
        int pivot_idx = partition(list, comparator);
        sort(list.subList(0,pivot_idx), comparator);
        sort(list.subList(pivot_idx+1, list.size()), comparator);
        // since we are passing a sublist then we are passing a view to the same array
        // and the partition method operates on the original array itself
        return list;
        // so it utilizes the sorting-in-place feature of QuickSort and we simply
        // return the original array
    }

    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list) {
        return List.of();
    }
}
