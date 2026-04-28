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
import java.io.IOException;
import java.util.Optional;

public class InternMenuController {

    @FXML
    private Button btnLogout;

    @FXML
    private void requestProjectAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/RequestProjectView.fxml", "Solicitar Proyecto");
    }

    @FXML
    private void partialReportsAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/PartialReportsView.fxml", "Gestión de Reportes Parciales");
    }

    @FXML
    private void monthlyReportsAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/MonthlyReportsView.fxml", "Gestión de Reportes Mensuales");
    }

    @FXML
    private void addActivityAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/AddActivityView.fxml", "Entregar Actividad");
    }

    @FXML
    private void addScheduleAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/AddScheduleView.fxml", "Añadir Horario");
    }

    @FXML
    private void pspBitacoraAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/PspBitacoraView.fxml", "Bitácora PSP");
    }

    @FXML
    private void activityPlanAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/ActivityPlanView.fxml", "Plan de Actividades");
    }

    @FXML
    private void selfEvaluationAction(ActionEvent event) {
        loadWindow("/mx/uv/spp/presentation/views/SelfEvaluationView.fxml", "Autoevaluación");
    }

    @FXML
    private void logoutAction(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cerrar Sesión");
        confirmation.setHeaderText(null);
        confirmation.setContentText("¿Seguro que desea salir del sistema?"); // IU-N-9 [cite: 454]

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
            e.printStackTrace();
        }
    }
}