package mx.uv.spp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("presentation/views/LoginView.fxml");

        Parent root = FXMLLoader.load(fxmlLocation);

        Scene scene = new Scene(root, 500, 550);
        primaryStage.setTitle("Inicio de Sesion - SPP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}