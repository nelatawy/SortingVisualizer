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
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class ComparisonController implements Initializable {

    @FXML
    private VBox toast;

    @FXML
    private Label toastHeader;

    static class SortStat {

        final StringProperty algorithm = new SimpleStringProperty();
        final IntegerProperty size = new SimpleIntegerProperty();
        final LongProperty comparisons = new SimpleLongProperty();
        final LongProperty swaps = new SimpleLongProperty();
        final LongProperty writes = new SimpleLongProperty();
        final DoubleProperty minRuntime = new SimpleDoubleProperty();
        final DoubleProperty maxRuntime = new SimpleDoubleProperty();
        final DoubleProperty meanRuntime = new SimpleDoubleProperty();

        public SortStat(String algorithm,int size,
                        long comparisons, long swaps, long writes,
                        double minRuntime, double maxRuntime, double meanRuntime) {
            this.algorithm.set(algorithm);
            this.size.set(size);
            this.comparisons.set(comparisons);
            this.swaps.set(swaps);
            this.writes.set(writes);
            this.minRuntime.set(minRuntime);
            this.maxRuntime.set(maxRuntime);
            this.meanRuntime.set(meanRuntime);
        }
        public String toString() {
            return  this.algorithm.get() + "," +
                    this.size.get() + "," +
                    this.comparisons.get() + "," +
                    this.swaps.get() + "," +
                    this.writes.get() + "," +
                    this.minRuntime.get() + "," +
                    this.maxRuntime.get() + "," +
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

        TableColumn<SortStat, Number> sizes = new TableColumn<>("Size");
        sizes.setCellValueFactory(data -> data.getValue().size);

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

        algorithmStats.getColumns().addAll(name,sizes,comparisons, swaps, writes, minRuntime, maxRuntime, meanRuntime);

    }

    @FXML
    private void runComparison(ActionEvent actionEvent) {
        Thread sortingThread = new Thread(()->{
            System.out.println(runCount.getText());
            for (Node node : algorithmCheckboxes.getChildren()){
                    addAlgorithmStats((CheckBox)  node);

            }
        });
        sortingThread.start();
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

        SortingStrategy<Integer> strategy = CommonUtils.getSortingAlgorithm(SortingAlgorithm.valueOf(checkBox.getText()));

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            SortingMetrics<Integer> metrics = new SortingMetrics<>();
            strategy.sort(new ArrayList<>(arr), Comparator.naturalOrder(), metrics);
            long end = System.nanoTime();
            double runtime = (end - start)/1e6;

            comparisonCnt += metrics.getComparisonCount();
            swapCnt += metrics.getSwapCount();
            setCnt += metrics.getSetCount();

            totalRuntime += runtime;
            minRuntime = Math.min(minRuntime, runtime);
            maxRuntime = Math.max(maxRuntime, runtime);

        }
        meanRuntime = totalRuntime / runs;

        algorithmStats.getItems().add(
                new SortStat(
                        checkBox.getText(),
                        arr.size(),
                        comparisonCnt/runs, swapCnt/runs, setCnt/runs,
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


        Path path = Path.of("stats.csv");
        if (!Files.exists(path)){
            outStr.append("Algorithm,Size,Comparisons,Swaps,Writes,Min Runtime,Max Runtime,Mean Runtime\n");
            Files.createFile(path);
        }
        for (SortStat stat : algorithmStats.getItems() ){
            outStr.append(stat.toString()).append("\n");
        }
        Files.writeString(path,outStr.toString(), StandardOpenOption.APPEND);

        toastHeader.setText("Saved stats to " +  path.toString());
        CommonUtils.showToast(toast, 3);
        return;

    }

    @FXML
    private void goToArrayGen(ActionEvent actionEvent) throws IOException {
        CommonUtils.goTo(actionEvent, "arrayGen");
    }



}
