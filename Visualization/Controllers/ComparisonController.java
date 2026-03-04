package Visualization.Controllers;

import Metrics.SortingMetrics;
import Strategies.SortingStrategy;
import Visualization.Enums.SortingAlgorithm;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ComparisonController implements Initializable {



    static class SortStat {

        final StringProperty algorithm = new SimpleStringProperty();
        final LongProperty comparisons = new SimpleLongProperty();
        final LongProperty swaps = new SimpleLongProperty();
        final LongProperty writes = new SimpleLongProperty();
        final DoubleProperty minRuntime = new SimpleDoubleProperty();
        final DoubleProperty maxRuntime = new SimpleDoubleProperty();
        final DoubleProperty meanRuntime = new SimpleDoubleProperty();

        public SortStat(String algorithm, long comparisons, long swaps, long writes, double minRuntime, double maxRuntime, double meanRuntime) {
            this.algorithm.set(algorithm);
            this.comparisons.set(comparisons);
            this.swaps.set(swaps);
            this.writes.set(writes);
            this.minRuntime.set(minRuntime);
            this.maxRuntime.set(maxRuntime);
            this.meanRuntime.set(meanRuntime);
        }
        public String toString() {
            return  this.algorithm.get() + "," +
                    this.comparisons.get() + "," +
                    this.swaps.get() + "," +
                    this.writes.get() + "," +
                    this.minRuntime.get() + "," +
                    this.meanRuntime.get();
        }

    }


    @FXML
    private HBox algorithmCheckboxes;

    @FXML
    private TextField runCount;

    @FXML
    private TableView<SortStat> algorithmStats;


    private List<Integer> arr;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(SortingAlgorithm algorithm : SortingAlgorithm.values()){
            CheckBox checkBox = new CheckBox(algorithm.toString());
            algorithmCheckboxes.getChildren().add(checkBox);
        }
        arr = ArrayVisManager.getInstance().getArraySnapshot(); //already generated from ArrayGenerator

        runCount.setTextFormatter(CommonUtils.createNumberFormatter());

        TableColumn<SortStat, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(data -> data.getValue().algorithm);

        TableColumn<SortStat, Number> comparisons = new TableColumn<>("Comparisons");
        comparisons.setCellValueFactory(data -> data.getValue().comparisons);

        TableColumn<SortStat, Number> swaps = new TableColumn<>("Swaps");
        swaps.setCellValueFactory(data -> data.getValue().swaps);

        TableColumn<SortStat, Number> writes = new TableColumn<>("Writes");
        writes.setCellValueFactory(data -> data.getValue().writes);

        TableColumn<SortStat, Number> maxRuntime = new TableColumn<>("Max Runtime");
        maxRuntime.setCellValueFactory(data -> data.getValue().maxRuntime);

        TableColumn<SortStat, Number> minRuntime = new TableColumn<>("Min Runtime");
        minRuntime.setCellValueFactory(data -> data.getValue().minRuntime);

        TableColumn<SortStat, Number> meanRuntime = new TableColumn<>("Mean Runtime");
        meanRuntime.setCellValueFactory(data -> data.getValue().meanRuntime);

        algorithmStats.getColumns().addAll(name, comparisons, swaps, writes, minRuntime, maxRuntime, meanRuntime);

    }

    @FXML
    private void runComparison(ActionEvent actionEvent) {
        System.out.println(runCount.getText());
        for (Node node : algorithmCheckboxes.getChildren()){
            addAlgorithmStats((CheckBox)  node);
        }
    }

    private void addAlgorithmStats(CheckBox checkBox) {
        if(!checkBox.isSelected())
            return;
        int runs = Integer.parseInt(runCount.getText());

        long comparisonCnt = 0;
        long swapCnt = 0;
        long setCnt = 0;
        double maxRuntime = 0L;
        double minRuntime = 1e9;
        double totalRuntime = 0L;
        double meanRuntime = 0L;

        HBox algoStats = new HBox();
        algoStats.getChildren().add(new Label(checkBox.getText()));
        SortingMetrics<Integer> metrics = new SortingMetrics<>();
        SortingStrategy<Integer> strategy = CommonUtils.getSortingAlgorithm(SortingAlgorithm.valueOf(checkBox.getText()));

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            strategy.sort(new ArrayList<>(arr), Comparator.naturalOrder(), metrics);
            long end = System.nanoTime();
            double runtime = (end - start)/1e6;

            comparisonCnt = metrics.getComparisonCount();
            swapCnt = metrics.getSwapCount();
            setCnt = metrics.getSetCount();

            totalRuntime += runtime;
            minRuntime = Math.min(minRuntime, runtime);
            maxRuntime = Math.max(maxRuntime, runtime);

        }
        meanRuntime = totalRuntime / runs;

        algorithmStats.getItems().add(
                new SortStat(
                        checkBox.getText(),
                        comparisonCnt, swapCnt, setCnt,
                        minRuntime, maxRuntime, meanRuntime
                ));
    }

    @FXML
    private void resetAlgorithmStats(ActionEvent actionEvent) {
        algorithmStats.getItems().clear();
    }

    @FXML
    private void saveAlgorithmStats(ActionEvent actionEvent) throws IOException {
        StringBuilder outStr = new StringBuilder();
        outStr.append("Algorithm,Comparisons,Swaps,Writes,Min Runtime,Max Runtime,Mean Runtime\n");
        for (SortStat stat : algorithmStats.getItems() ){
            outStr.append(stat.toString()).append("\n");
        }
        Files.writeString(Path.of("log" + UUID.randomUUID() + ".csv"),outStr.toString());
        return;

    }



}
