import DSProxies.TrackingComparator;
import DSProxies.TrackedList;
import Metrics.SortingMetrics;
import Strategies.*;

import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {
        SortingStrategy strategy = new QuickSort();
        SortingMetrics<Integer> metrics = new SortingMetrics<>();
        TrackedList<Integer> list = new TrackedList<>(ArrayGenerator.generateRandomArray((int)1024), metrics);
//        list.forEach((e) -> System.out.print(e + " "));
//        System.out.println();

        TrackingComparator<Integer> comparator = new TrackingComparator<>(Integer::compareTo);

        strategy.sort(list, comparator);

        System.out.println(list.getOps().size());
        System.out.println(comparator.getComparisonCount());
//        list.forEach((e) -> System.out.print(e + " "));
//        System.out.println();


    }
}
