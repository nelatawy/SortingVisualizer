package Metrics.Steps;

import Visualization.TonePlayer;
import Visualization.VisualizationBars;

import javax.sound.sampled.LineUnavailableException;

public class SwapStep<E> implements SortingStep<E> {

    public int firstIdx;
    public int secondIdx;

    public SwapStep(int firstIdx, int secondIdx) {
        this.firstIdx = firstIdx;
        this.secondIdx = secondIdx;
    }
    @Override
    public void visualizeOn(VisualizationBars<E> visualizationBars) {
        int len = visualizationBars.bars.size();
        E first = visualizationBars.values.get(firstIdx);
        E second = visualizationBars.values.get(secondIdx);

        visualizationBars.updateHeightAt(firstIdx, second);
        visualizationBars.updateHeightAt(secondIdx, first);

        visualizationBars.markBarAt(this.firstIdx, VisualizationBars.Label.FOCUS);
        try{
            TonePlayer.getInstance().playTone(150 * firstIdx / len , 50);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        visualizationBars.markBarAt(this.secondIdx, VisualizationBars.Label.FOCUS);
        try{
            TonePlayer.getInstance().playTone(150 * secondIdx / len , 50);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
