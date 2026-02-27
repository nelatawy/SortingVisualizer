package Metrics.Steps;

import Visualization.VisualizationBars;

public class SwapStep<E> implements SortingStep<E> {

    public int firstIdx;
    public int secondIdx;

    public SwapStep(int firstIdx, int secondIdx) {
        this.firstIdx = firstIdx;
        this.secondIdx = secondIdx;
    }
    @Override
    public void visualizeOn(VisualizationBars<E> visualizationBars) {
        E first = visualizationBars.values.get(firstIdx);
        E second = visualizationBars.values.get(secondIdx);

        visualizationBars.updateHeightAt(firstIdx, second);
        visualizationBars.updateHeightAt(secondIdx, first);

        visualizationBars.markBarAt(this.firstIdx, VisualizationBars.Label.FOCUS);
        visualizationBars.markBarAt(this.secondIdx, VisualizationBars.Label.FOCUS);
    }
}
