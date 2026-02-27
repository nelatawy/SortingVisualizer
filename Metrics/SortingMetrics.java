package Metrics;

import Metrics.Steps.CompareStep;
import Metrics.Steps.SetStep;
import Metrics.Steps.SortingStep;

import java.util.ArrayList;
import java.util.List;

public class SortingMetrics<E> {
    private int comparisonCount;
    private int setsCount;
    private List<SortingStep<E>> steps;

    public SortingMetrics() {
        steps = new ArrayList<SortingStep<E>>();
    }

    public void addStep(SortingStep<E> step) {
        if (step instanceof CompareStep<E>) {
            comparisonCount++;
        }
        else if (step instanceof SetStep<E>) {
            setsCount++;
        }
        steps.add(step);
    }

}
