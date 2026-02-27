package Metrics.Steps;

import Visualization.VisualizationBars;

public class CompareStep<E> implements SortingStep<E> {
    int firstIdx;
    int secondIdx;
    public CompareStep(int firstIdx, int secondIdx) {
        this.firstIdx = firstIdx;
        this.secondIdx = secondIdx;
    }
    @Override
    public void visualizeOn(VisualizationBars<E> visualizationBars) {
        if (Math.max(this.firstIdx, this.secondIdx) >= visualizationBars.bars.size()) {
            return;
        }
        visualizationBars.markBarAt(this.firstIdx, VisualizationBars.Label.HIGHLIGHT);
        visualizationBars.markBarAt(this.secondIdx, VisualizationBars.Label.HIGHLIGHT);

    }
}
