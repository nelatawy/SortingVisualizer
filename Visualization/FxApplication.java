package Visualization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FxApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Visualization/fxml/arrayGen.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Visualization/style.css").toExternalForm());


        stage.setTitle("Sorting Visualizer");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
