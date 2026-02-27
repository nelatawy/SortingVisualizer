package Strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BubbleSort implements SortingStrategy{

    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list, Comparator<E> comparator) {
        for (int i = 0; i < list.size(); i++){
            for (int j = 0; j < list.size() - 1; j++){
                if (comparator.compare(list.get(j + 1),list.get(j)) < 0){
                    Collections.swap(list, j, j + 1);
                }
            }
        }
        return list;
    }

    @Override
    public <E extends Comparable<E>> List<E> sort(List<E> list) {
        return sort(list, Comparator.naturalOrder());
    }
}
