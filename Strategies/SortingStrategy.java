package Strategies;

import java.util.Comparator;
import java.util.List;

public interface SortingStrategy {


    public <E extends Comparable<E>> List<E> sort(List<E> list, Comparator<E> comparator);
    public <E extends Comparable<E>> List<E> sort(List<E> list);
}
