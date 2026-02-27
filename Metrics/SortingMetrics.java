package Metrics;

import Metrics.Steps.CompareStep;
import Metrics.Steps.SetStep;
import Metrics.Steps.SortingStep;
import Metrics.Steps.SwapStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingMetrics<E> {
    private int comparisonCount;
    private int setCount;
    private int swapCount;
    private final List<SortingStep<E>> steps;

    public SortingMetrics() {
        steps = new ArrayList<SortingStep<E>>();
    }

    public void addStep(SortingStep<E> step) {
        if (step instanceof CompareStep<E>) {
            comparisonCount++;
        }
        else if (step instanceof SetStep<E>) {
            setCount++;
        }
        else if (step instanceof SwapStep<E>) {
            swapCount++;
        }
        steps.add(step);
    }

    public int getComparisonCount() {
        return comparisonCount;
    }

    public int getSwapCount() {
        return swapCount;
    }

    public int getSetCount() {
        return setCount;
    }

    public List<SortingStep<E>> getSteps() {
        return Collections.unmodifiableList(steps);
    }
}
