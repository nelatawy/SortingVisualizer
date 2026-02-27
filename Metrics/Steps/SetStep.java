package Metrics.Steps;

import Visualization.VisualizationBars;

public class SetStep<E> implements SortingStep<E> {
    int index;
    E value;

    public SetStep(int index, E value) {
        this.index = index;
        this.value = value;
    }
    @Override
    public void visualizeOn(VisualizationBars<E> visualizationBars) {
        visualizationBars.updateHeightAt(index, value);
        visualizationBars.markBarAt(index, VisualizationBars.Label.FOCUS);
    }
}
