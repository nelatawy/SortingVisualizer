package Visualization;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class VisualizationBars<T> {
    public List<Rectangle> bars;
    public List<T> values;
    private final ToIntFunction<T> mappingFunction;
    public List<IntegerProperty> heights;

    public enum Label {
        HIGHLIGHT,
        FOCUS,
        NONE,
    }

    public VisualizationBars(ToIntFunction<T> mapper) {
        bars = new ArrayList<Rectangle>();
        mappingFunction = mapper;
        values = new ArrayList<>();
        heights = new ArrayList<>();

    }

    public void addBar(Rectangle bar, T value) {
        IntegerProperty height = new SimpleIntegerProperty(mappingFunction.applyAsInt(value));
        bar.heightProperty().bind(height.multiply(1));
        bars.add(bar);
        values.add(value);
        heights.add(height);
    }

    public void updateHeightAt(int index, T value) {
        values.set(index, value);
        heights.get(index).set(mappingFunction.applyAsInt(value));

    }

    public void markBarAt(int idx, Label label) {
        Rectangle target = bars.get(idx);
        switch (label) {
            case HIGHLIGHT:
                target.setFill(Color.LIGHTBLUE);
                break;
            case FOCUS:
                target.setFill(Color.ORANGE);
                break;
            case NONE:
                target.setFill(Color.BLUE);
                break;
        }
    }

    public void resetStyling(){
        for(int i = 0; i < bars.size(); i++){
            markBarAt(i,Label.NONE);
        }
    }

    public void clearBars(){
        bars.clear();
        values.clear();
        heights.clear();
    }
}
