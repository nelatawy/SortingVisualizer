package Visualization.Controllers;

import Visualization.Enums.ArrayGenMode;
import Visualization.VisualizationBars;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Generator.ArrayGenerator;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class arrayGenController implements Initializable {

    @FXML
    private ComboBox<ArrayGenMode> arrayModeSelector;

    @FXML
    private TextField fromField;
    @FXML
    private TextField toField;
    @FXML
    private TextField size;

    @FXML
    private HBox barContainer;

    VisualizationBars<Integer> visualizationBars;

    List<Integer> arr;

    private TextFormatter<String> createNumberFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= 3) {
                return change;
            }
            return null;
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        visualizationBars = new VisualizationBars<>(e -> e);
        arr = new ArrayList<>();

        arrayModeSelector.getItems().addAll(ArrayGenMode.values());
        arrayModeSelector.setValue(ArrayGenMode.RANDOM);

        fromField.setTextFormatter(createNumberFormatter());
        toField.setTextFormatter(createNumberFormatter());
        size.setTextFormatter(createNumberFormatter());

        fromField.setText("1");
        toField.setText("100");
        size.setText("20");

    }

    // generates an array and visualizes it
    @FXML
    public void genVisArray() {
        generateArray();
        linkToVisBars();

    }

    public void generateArray() {
        visualizationBars.clearBars();
        barContainer.getChildren().clear();

        int from = Integer.parseInt(fromField.getText());
        int to = Integer.parseInt(toField.getText());
        int arraySize = Integer.parseInt(size.getText());

        arr = switch (arrayModeSelector.getValue()) {
            case SORTED -> ArrayGenerator.generateSortedArray(arraySize, from, to, false);
            case REVERSED -> ArrayGenerator.generateSortedArray(arraySize, from, to, true);
            case RANDOM -> ArrayGenerator.generateRandomArray(arraySize, from, to);
            case FILE -> new ArrayList<>();
        };

        System.out.println(from + " " + to + " " + arraySize);
    }

    private void linkToVisBars() {
        barContainer.setAlignment(Pos.BOTTOM_LEFT);
        for (int i = 0; i < arr.size(); i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(0);
            rectangle.setWidth(barContainer.getPrefWidth() / arr.size());
            rectangle.setFill(Color.BLUE);
            rectangle.getStyleClass().add("bar");
            visualizationBars.addBar(rectangle, arr.get(i));
            barContainer.getChildren().add(rectangle);
        }


        ArrayVisManager.getInstance().loadSnapshot(arr, visualizationBars);
    }

    @FXML
    private void goToVisualizer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Visualization/fxml/visualizer.fxml"));

        Parent newRoot = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.getScene().setRoot(newRoot);
    }

}
