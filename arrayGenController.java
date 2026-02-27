import Metrics.SortingMetrics;
import Metrics.Steps.SortingStep;
import Strategies.*;
import Visualization.SortingAlgorithm;
import Visualization.VisualizationBars;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

public class arrayGenController implements Initializable {

    @FXML
    private ComboBox<SortingAlgorithm> algorithmSelector;

    @FXML
    private TextField fromField;
    @FXML
    private TextField toField;
    @FXML
    private TextField size;

    @FXML
    private HBox barContainer;

    VisualizationBars<Integer> barsVisualization;

    List<Integer> arr;

    private TextFormatter<String> createNumberFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barsVisualization = new VisualizationBars<>(e -> e);

        algorithmSelector.getItems().addAll(SortingAlgorithm.values());
        algorithmSelector.setValue(SortingAlgorithm.BUBBLE);

        fromField.setTextFormatter(createNumberFormatter());
        toField.setTextFormatter(createNumberFormatter());
        size.setTextFormatter(createNumberFormatter());

    }

    public void generateRandomArray() {
        barsVisualization.clearBars();
        barContainer.getChildren().clear();

        arr = ArrayGenerator.generateRandomArray(10);
        for (int i = 0; i < arr.size(); i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(0);
            rectangle.setWidth(barContainer.widthProperty().get() / arr.size());
            rectangle.setFill(Color.BLUE);
            barsVisualization.addBar(rectangle, arr.get(i));
//            rectangle.widthProperty().bind(barContainer.widthProperty().divide(arr.size()));
            barContainer.getChildren().add(rectangle);
        }
        barContainer.setAlignment(Pos.BOTTOM_LEFT);
    }

    private SortingStrategy<Integer> getSortingAlgorithm(){
        return switch (algorithmSelector.getValue()) {
            case BUBBLE -> new BubbleSort<>();
            case QUICK -> new QuickSort<>();
            case HEAP -> new HeapSort<>();
            case INSERTION -> new InsertionSort<>();
            case SELECTION -> new SelectionSort<>();
            case MERGE -> new MergeSort<>();
        };
    }

    public void sort() throws InterruptedException {
        SortingStrategy<Integer> sortingAlgorithm = getSortingAlgorithm();
        SortingMetrics<Integer> metrics = new SortingMetrics<>();
        sortingAlgorithm.sort(arr, Comparator.naturalOrder(), metrics);
        for (SortingStep<Integer> step : metrics.getSteps()) {
            step.visualizeOn(barsVisualization);
        }
        barsVisualization.resetStyling();
        for (Integer num : arr){
            System.out.print(num + " ");
        }
        System.out.println();

    }





}
