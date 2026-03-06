package Visualization.Controllers;

import Metrics.SortingMetrics;
import Metrics.Steps.SortingStep;
import Strategies.*;
import Visualization.Enums.SortingAlgorithm;
import Visualization.VisualizationBars;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class sortingController implements Initializable {

    @FXML
    private ComboBox<SortingAlgorithm> algorithmSelector;

    @FXML
    private HBox barContainer;

    @FXML
    private Slider speedSlider;


    @FXML
    private VBox toast;

    @FXML
    private Label swapsLabel;

    @FXML
    private Label comparisonsLabel;

    @FXML
    private Label writesLabel;



    private Timeline timeline;

    SortingMetrics<Integer> metrics;

    VisualizationBars<Integer> visualizationBars;

    List<Integer> arr;

    boolean isVisualizing;

    boolean isPaused;

    boolean priorlySorted;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barContainer.setAlignment(Pos.BOTTOM_LEFT);
        reset();
        // we are only taking a copy of the system-wide data
        // so that we can replay without messing with the system-wide data
        metrics = new SortingMetrics<>();

        algorithmSelector.getItems().addAll(SortingAlgorithm.values());
        algorithmSelector.setValue(SortingAlgorithm.BUBBLE);

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            timeline.setRate(newVal.doubleValue());
        });

        isPaused = false;
        priorlySorted = false;
    }

    private void reset(){
        arr = ArrayVisManager.getInstance().getArraySnapshot();
        visualizationBars = ArrayVisManager.getInstance().getVisualizationBarsSnapshot();
        barContainer.getChildren().clear();
        for (Rectangle bar : visualizationBars.bars) {
            barContainer.getChildren().add(bar);
        }
        timeline = new Timeline();
        metrics = new SortingMetrics<>();
    }

    @FXML
    private void pause(){
        timeline.pause();
        isPaused = true;
    }

    @FXML
    private void resume(){
        timeline.play();
        isPaused = false;
    }


    @FXML
    public void visualizeSort() {
        if (isVisualizing) {
            return;
        }
        if (priorlySorted) {
            reset();
        }
        // to allow for resets
        SortingStrategy<Integer> sortingAlgorithm = CommonUtils.getSortingAlgorithm(algorithmSelector.getValue());
        sortingAlgorithm.sort(arr, Comparator.naturalOrder(), metrics);
        priorlySorted = true;
        new Thread(() -> {
            visualize(metrics.getSteps());
        }).start();


    }

    public void visualize(List<SortingStep<Integer>> steps) {
        isVisualizing = true;

        for (int i = 0; i < steps.size(); i++) {
            int idx = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(1000/24.0 * (i + 1)), e -> {
                visualizationBars.resetStyling();
                steps.get(idx).visualizeOn(visualizationBars);
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        KeyFrame sortedKF = new KeyFrame(Duration.millis(1000/24.0 * (steps.size() + 1)), e -> {
            visualizationBars.markAllSorted();
        });
        timeline.getKeyFrames().add(sortedKF);
        timeline.setOnFinished(e -> {
            isVisualizing = false;
            showStats();
        });
        timeline.play();
    }

    void showStats(){
        swapsLabel.setText(String.valueOf(metrics.getSwapCount()));
        comparisonsLabel.setText(String.valueOf(metrics.getComparisonCount()));
        writesLabel.setText(String.valueOf(metrics.getSetCount()));

        TranslateTransition fadeIn = new TranslateTransition(Duration.millis(300), toast);
        fadeIn.setByY(-400);

        PauseTransition stay = new PauseTransition(Duration.seconds(5));

        TranslateTransition fadeOut = new TranslateTransition(Duration.millis(300), toast);
        fadeOut.setByY(400);

        SequentialTransition seq = new SequentialTransition(fadeIn, stay, fadeOut);
        seq.play();
    }
    @FXML
    private void goToArrayGen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Visualization/fxml/arrayGen.fxml"));

        Parent newRoot = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.getScene().setRoot(newRoot);
    }
}
