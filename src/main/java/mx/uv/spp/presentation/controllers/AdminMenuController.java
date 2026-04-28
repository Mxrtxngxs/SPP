package mx.uv.spp.presentation.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import mx.uv.spp.utils.LogConfig;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class AdminMenuController {

    @FXML
    private Button btnRegisterCoordinator;

    @FXML
    private Button btnRegisterProfessor;

    @FXML
    private Button btnInactivateCoordinator;

    @FXML
    private Button btnInactivateProfessor;

    @FXML
    private Button btnLogout;

    private static final Logger LOG = LogConfig.getLogger(AdminMenuController.class);

    @FXML
    private void registerCoordinatorAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/RegisterCoordinatorView.fxml", "Registrar Coordinador");
    }

    @FXML
    private void registerProfessorAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/RegisterProfessorView.fxml", "Registrar Profesor");
    }

    @FXML
    private void inactivateCoordinatorAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/InactivateCoordinatorView.fxml", "Inactivar Coordinador");
    }

    @FXML
    private void inactivateProfessorAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/InactivateProfessorView.fxml", "Inactivar Profesor");
    }

    @FXML
    private void logoutAction(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cerrar Sesión");
        confirmation.setHeaderText(null);
        confirmation.setContentText("¿Seguro que desea salir del sistema?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadWindow("/mx/uv/spp/presentation/views/LoginView.fxml", "Inicio de Sesión - SPP");
        }
    }

    private void loadWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            LOG.severe("Error loading UI: " + e.getMessage());
            showErrorAlert("No se pudo cargar la ventana: " + title);
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Sistema");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}