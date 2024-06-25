package zadanie.wzi.wzi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WziZadanie extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WziZadanie.class.getResource("glowny_widok.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1070, 500);
        stage.setTitle("Obrazowanie obrazu z plików DICOM!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}