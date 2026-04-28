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

public class ProfessorMenuController {

    @FXML
    private Button btnLogout;

    private static final Logger LOG = LogConfig.getLogger(ProfessorMenuController.class);

    @FXML
    private void generateActivityAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/GenerateActivityView.fxml", "Generar Actividad");
    }

    @FXML
    private void addFormatAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/AddFormatView.fxml", "Añadir Formato de Presentación");
    }

    @FXML
    private void evaluatePartialReportAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/EvaluatePartialReportView.fxml", "Evaluar Reporte Parcial");
    }

    @FXML
    private void evaluateMonthlyReportAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/EvaluateMonthlyReportView.fxml", "Evaluar Reporte Mensual");
    }

    @FXML
    private void messagesAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/MessagesView.fxml", "Buzón de Mensajes");
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