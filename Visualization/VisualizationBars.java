package Visualization;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;

public class VisualizationBars<T> {
    public List<Rectangle> bars;
    public List<T> values;
    private final ToIntFunction<T> mappingFunction;
    public List<DoubleProperty> heights;
    private final DoubleProperty max;
    private final DoubleProperty containerHeight;

    public enum Label {
        SORTED,
        HIGHLIGHT,
        FOCUS,
        NONE,
    }

    public VisualizationBars(ToIntFunction<T> mapper, double containerHeight) {
        this.bars = new ArrayList<Rectangle>();
        this.mappingFunction = mapper;
        this.values = new ArrayList<>();
        this.heights = new ArrayList<>();
        this.containerHeight = new SimpleDoubleProperty(containerHeight);
        this.max = new SimpleDoubleProperty((int)-1e9);
    }

    public VisualizationBars(VisualizationBars<T> other) {
        this.mappingFunction = other.mappingFunction;
        this.bars = new ArrayList<>();
        this.values = new ArrayList<>(other.values);
        this.heights = new ArrayList<>();
        this.containerHeight = new SimpleDoubleProperty(other.containerHeight.get());
        this.max = new SimpleDoubleProperty(other.max.get());

        for (int i = 0; i < other.bars.size(); i++) {
            Rectangle copy = copyRectangle(other.bars.get(i));
            addBar(copy, this.values.get(i));
        }
    }

    public static Rectangle copyRectangle(Rectangle original) {
        Rectangle copy = new Rectangle();
        copy.setWidth(original.getWidth());
        copy.setHeight(original.getHeight());
        copy.getStyleClass().addAll(original.getStyleClass());

        return copy;
    }

    public void addBar(Rectangle bar, T value) {
        int intVal = mappingFunction.applyAsInt(value);

        if (intVal > max.get()) {
            max.set(intVal);
        }
        DoubleProperty height = new SimpleDoubleProperty(intVal);
        bar.heightProperty().bind(height.divide(max).multiply(containerHeight));
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
        target.getStyleClass().removeAll("bar-sorted", "bar-highlight", "bar-focus");

        switch (label) {
            case SORTED:
                target.getStyleClass().add("bar-sorted");
                break;
            case HIGHLIGHT:
                target.getStyleClass().add("bar-highlight");
                break;
            case FOCUS:
                target.getStyleClass().add("bar-focus");
                break;
            case NONE:
                break;
        }
    }

    public void resetStyling() {
        for (int i = 0; i < bars.size(); i++) {
            markBarAt(i, Label.NONE);
        }
    }

    public void clearBars() {
        max.set(-1e9);
        bars.clear();
        values.clear();
        heights.clear();
    }

    public void markAllSorted() {
        for (int i = 0; i < bars.size(); i++) {
            markBarAt(i, Label.SORTED);
        }
    }
}
