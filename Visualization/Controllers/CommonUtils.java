package Visualization.Controllers;

import Strategies.*;
import Visualization.Enums.SortingAlgorithm;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.IOException;

public class CommonUtils {
    public static TextFormatter<String> createNumberFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= 4) {
                return change;
            }
            return null;
        });
    }

    public static void goTo(ActionEvent event, String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                CommonUtils.class.getResource("/Visualization/fxml/" + fxmlName + ".fxml"));

        Parent newRoot = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.getScene().setRoot(newRoot);
    }
    public static SortingStrategy<Integer> getSortingAlgorithm(SortingAlgorithm sortingAlgorithm) {
        return switch (sortingAlgorithm) {
            case BUBBLE -> new BubbleSort<>();
            case QUICK -> new QuickSort<>();
            case HEAP -> new HeapSort<>();
            case INSERTION -> new InsertionSort<>();
            case SELECTION -> new SelectionSort<>();
            case MERGE -> new MergeSort<>();
        };
    }

}
