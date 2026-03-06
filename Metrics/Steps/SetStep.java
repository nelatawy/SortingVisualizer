package Metrics.Steps;

import Visualization.TonePlayer;
import Visualization.VisualizationBars;

import javax.sound.sampled.LineUnavailableException;

public class SetStep<E> implements SortingStep<E> {
    int index;
    E value;

    public SetStep(int index, E value) {
        this.index = index;
        this.value = value;
    }
    @Override
    public void visualizeOn(VisualizationBars<E> visualizationBars) {
        int len = visualizationBars.bars.size();
        visualizationBars.updateHeightAt(index, value);
        visualizationBars.markBarAt(index, VisualizationBars.Label.FOCUS);
        try{
            TonePlayer.getInstance().playTone(200 * index / len , 50);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
