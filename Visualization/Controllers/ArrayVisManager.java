package Visualization.Controllers;

import Visualization.VisualizationBars;

import java.util.ArrayList;
import java.util.List;

public class ArrayVisManager {
    private static ArrayVisManager instance;

    private List<Integer> array;
    private VisualizationBars<Integer> visualizationBars;

    public static ArrayVisManager getInstance() {
        if (instance == null) {
            instance = new ArrayVisManager();
        }
        return instance;
    }

    public void loadSnapshot(List<Integer> array, VisualizationBars<Integer> visualizationBars) {
        this.array = new ArrayList<>(array);
        this.visualizationBars = new VisualizationBars<>(visualizationBars);
    }

    public List<Integer> getArraySnapshot() {
        return new ArrayList<>(array);
    }

    public VisualizationBars<Integer> getVisualizationBarsSnapshot() {
        return new VisualizationBars<>(visualizationBars);
    }
}
