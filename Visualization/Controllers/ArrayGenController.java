package Visualization.Controllers;

import Visualization.Enums.ArrayGenMode;
import Visualization.VisualizationBars;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import Generator.ArrayGenerator;
import javafx.stage.FileChooser;

import javax.swing.plaf.multi.MultiFileChooserUI;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static Visualization.Controllers.CommonUtils.createNumberFormatter;

public class ArrayGenController implements Initializable {


    @FXML
    private VBox toast;

    @FXML
    private Label toastHeader;

    @FXML
    private ComboBox<ArrayGenMode> arrayModeSelector;


    private TextField fromField;

    private TextField toField;

    private TextField size;

    @FXML
    private HBox paramBox;

    private FileChooser fileChooser;

    private List<File> selectedFiles;

    private Label fileNames;

    @FXML
    private HBox barContainer;

    @FXML
    private Button goToVisualizeBtn;

    @FXML
    private Button goToComparisonBtn;

    VisualizationBars<Integer> visualizationBars;

    List<Integer> arr;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        visualizationBars = new VisualizationBars<Integer>(e-> e, (int)barContainer.getPrefHeight());
        arr = new ArrayList<>();

        arrayModeSelector.getItems().addAll(ArrayGenMode.values());
        fileChooser = new FileChooser();
        fileNames = new Label();

        fromField = new TextField();
        toField = new TextField();
        size = new TextField();
//        arrayModeSelector.setValue(ArrayGenMode.RANDOM);

        fromField.setTextFormatter(createNumberFormatter());
        toField.setTextFormatter(createNumberFormatter());
        size.setTextFormatter(createNumberFormatter());

        fromField.setText("1");
        toField.setText("100");
        size.setText("20");
        goToVisualizeBtn.setDisable(true);
        goToComparisonBtn.setDisable(true);

    }

    // generates an array and visualizes it
    @FXML
    public void genVisArray() throws IOException {
        generateArray();
        if (arr.isEmpty())
            return;

        goToComparisonBtn.setDisable(false);


        if (arr.size() > 250) {
            ArrayVisManager.getInstance().loadSnapshot(arr, visualizationBars); //adding them as is
            goToVisualizeBtn.setDisable(true); // prevent user from trying to visualize it
            toastHeader.setText("Array has been generated but is too large to visualize");
            CommonUtils.showToast(toast, 2);
            return;
        }
        goToVisualizeBtn.setDisable(false);
        toastHeader.setText("Array has been generated");
        CommonUtils.showToast(toast, 1);
        linkToVisBars();

    }

    public void generateArray() throws IOException {
        visualizationBars.clearBars();
        barContainer.getChildren().clear();

        int from = fromField.getText().isEmpty() ? -1 : Integer.parseInt(fromField.getText());
        int to = toField.getText().isEmpty() ? -1 : Integer.parseInt(toField.getText());
        int arraySize = size.getText().isEmpty() ? -1 : Integer.parseInt(size.getText());

        arr = switch (arrayModeSelector.getValue()) {
            case SORTED -> ArrayGenerator.generateSortedArray(arraySize, from, to, false);
            case REVERSED -> ArrayGenerator.generateSortedArray(arraySize, from, to, true);
            case RANDOM -> ArrayGenerator.generateRandomArray(arraySize, from, to);
            case FILE -> {
                List<Integer> array = new ArrayList<>();
                selectedFiles.forEach(file -> {
                    try {
                        array.addAll(ArrayGenerator.getArrayFromFile(file.toPath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                yield array;
            }
            case null -> new ArrayList<>();
        };

    }

    private void linkToVisBars() {
        barContainer.setAlignment(Pos.BOTTOM_LEFT);
        for (int i = 0; i < arr.size(); i++) {
            Rectangle rectangle = new Rectangle();
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
        CommonUtils.goTo(event, "visualizer");
    }

    @FXML
    private void goToComparison(ActionEvent event) throws IOException {
        CommonUtils.goTo(event, "comparison");
    }

    @FXML
    private void onModeSelected(ActionEvent event) throws IOException {
        ArrayGenMode mode = arrayModeSelector.getValue();
        if (mode == ArrayGenMode.FILE){
            paramBox.getChildren().clear();
            Button fileButton = new  Button("Select Files");
            fileButton.setOnAction(e -> {
                selectedFiles = fileChooser.showOpenMultipleDialog(((Node) event.getSource())
                        .getScene()
                        .getWindow());
                fileNames.setText(selectedFiles.stream().map(File::getName).collect(Collectors.joining(", ")) );
            });

            paramBox.getChildren().add(fileButton);
            paramBox.getChildren().add(fileNames);
        } else {
            paramBox.getChildren().clear();
            paramBox.getChildren().add(fromField);
            paramBox.getChildren().add(toField);
            paramBox.getChildren().add(size);

            fromField.setText("1");
            toField.setText("100");
            size.setText("20");
        }
    }

}
