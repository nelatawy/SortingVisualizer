package Generator;

import Strategies.MergeSort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArrayGenerator {

    public static List<Integer> generateRandomArray(int size, int from, int to) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            list.add(from + (int)(Math.random() * (to - from)));
        }
        return list;
    }

    public static List<Integer> generateRandomArray(int size) {
        return generateRandomArray(size, 0, size);
    }

    public static List<Integer> generateSortedArray(int size, int from, int to, boolean descending) {
        List<Integer> list = generateRandomArray(size, from, to);
        new MergeSort<Integer>().sort(list, (descending) ? Comparator.reverseOrder() : Comparator.naturalOrder());
        return list;
    }

    public static List<Integer> generateSortedArray(int size) {
        return generateSortedArray(size, 0, size, false);
    }

    public static List<Integer> generateSortedArray(int size, boolean descending) {
        return generateSortedArray(size, 0, size, descending);
    }

    public static List<Integer> getArrayFromFile(Path path) throws IOException, NumberFormatException {
        String fileStr = Files.readString(path);
        String[] elements = fileStr.split(",");
        List<Integer> list = new ArrayList<>();
        for (String ele : elements) {
            list.add(Integer.parseInt(ele.trim()));
        }
        return list;
    }
}
