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

public class CoordinatorMenuController {

    @FXML
    private Button btnLogout;

    private static final Logger LOG = LogConfig.getLogger(CoordinatorMenuController.class);

    @FXML
    private void registerProjectAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/RegisterProjectView.fxml", "Registrar Proyecto");
    }

    @FXML
    private void assignProjectAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/AssignProjectView.fxml", "Asignar Proyecto");
    }

    @FXML
    private void updateProjectAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/UpdateProjectView.fxml", "Actualizar Proyecto");
    }

    @FXML
    private void deleteProjectAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/DeleteProjectView.fxml", "Eliminar Proyecto");
    }

    @FXML
    private void registerInternAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/RegisterInternView.fxml", "Registrar Practicante");
    }

    @FXML
    private void inactivateInternAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/InactivateInternView.fxml", "Inactivar Practicante");
    }

    @FXML
    private void generateReportAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/GenerateReportView.fxml", "Reporte de Indicadores");
    }

    @FXML
    private void logoutAction(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cerrar Sesion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("¿Seguro que desea salir del sistema?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadWindow("/mx/uv/spp/presentation/views/LoginView.fxml", "Inicio de Sesion - SPP");
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