package mx.uv.spp.presentation.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private void openLogin(ActionEvent event) {
        openWindow("/mx/uv/spp/presentation/views/LoginView.fxml", "Login");
    }

    @FXML
    private void openRegisterCoordinator(ActionEvent event) {
        openWindow("/mx/uv/spp/presentation/views/RegisterCoordinatorView.fxml", "Registrar Coordinador");
    }

    @FXML
    private void openRegisterProfessor(ActionEvent event) {
        openWindow("/mx/uv/spp/presentation/views/RegisterProfessorView.fxml", "Registrar Profesor");
    }

    @FXML
    private void openListCoordinators(ActionEvent event) {
        openWindow("/mx/uv/spp/presentation/views/ListCoordinatorsView.fxml", "Lista de Coordinadores");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException | IllegalStateException e) {
            showAlert("Error", "No se pudo abrir la vista: " + fxmlPath);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}