import Generator.ArrayGenerator;
import Metrics.SortingMetrics;
import Strategies.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws IOException {
        SortingStrategy<Integer> strategy = new InsertionSort<>();
        SortingMetrics<Integer> metrics = new SortingMetrics<>();
        List<Integer> list = ArrayGenerator.generateRandomArray((int)30);
//        list.forEach((e) -> System.out.print(e + " "));
//        System.out.println();


        strategy.sort(list, Comparator.naturalOrder(), metrics);

        System.out.println(metrics.getSwapCount());
        System.out.println(metrics.getComparisonCount());
//        list.forEach((e) -> System.out.print(e + " "));
//        System.out.println();


    }
}
