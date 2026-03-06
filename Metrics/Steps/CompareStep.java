package Metrics.Steps;

import Visualization.TonePlayer;
import Visualization.VisualizationBars;

import javax.sound.sampled.LineUnavailableException;

public class CompareStep<E> implements SortingStep<E> {
    int firstIdx;
    int secondIdx;
    public CompareStep(int firstIdx, int secondIdx) {
        this.firstIdx = firstIdx;
        this.secondIdx = secondIdx;
    }
    @Override
    public void visualizeOn(VisualizationBars<E> visualizationBars) {
        int len = visualizationBars.bars.size();
        if (Math.max(this.firstIdx, this.secondIdx) >= visualizationBars.bars.size()) {
            return;
        }
        visualizationBars.markBarAt(this.firstIdx, VisualizationBars.Label.HIGHLIGHT);
        try{
            TonePlayer.getInstance().playTone(400 * firstIdx / len, 50);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        visualizationBars.markBarAt(this.secondIdx, VisualizationBars.Label.HIGHLIGHT);
        try{
            TonePlayer.getInstance().playTone(400 * secondIdx / len , 50);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }
}
