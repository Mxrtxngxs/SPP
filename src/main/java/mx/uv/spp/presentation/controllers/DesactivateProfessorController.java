package mx.uv.spp.presentation.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.service.IProfessorService;
import mx.uv.spp.business.service.implementations.ProfessorServiceImplementation;
import mx.uv.spp.utils.LogConfig;

import java.util.logging.Logger;

public class DesactivateProfessorController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblPersonalNumber;

    @FXML
    private Label lblShift;

    @FXML
    private Label lblState;

    private IProfessorService professorService;
    private ProfessorDTO professorSelected;

    private static final Logger LOG = LogConfig.getLogger(DesactivateProfessorController.class);

    public DesactivateProfessorController() {
        this.professorService = new ProfessorServiceImplementation();
    }

    public void setProfessor(ProfessorDTO professor) {
        this.professorSelected = professor;
        if (professor != null) {
            lblName.setText(professor.getName());
            lblPersonalNumber.setText(professor.getStaffNumber());
            lblShift.setText(professor.getShift());
            lblState.setText(professor.getState());
        }
    }

    @FXML
    private void desactivateProfessor(ActionEvent event) {
        if (isDataValid() && showConfirmation()) {
            executeDeactivation(event);
        }
    }

    private boolean isDataValid() {
        boolean isValid = true;
        if (professorService == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Servicio no disponible");
            isValid = false;
        } else if (professorSelected == null || professorSelected.getIdUser() <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Informacion del profesor no cargada");
            isValid = false;
        }
        return isValid;
    }

    private boolean showConfirmation() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmacion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Seguro que desea inactivar a este profesor");
        return confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void executeDeactivation(ActionEvent event) {
        if (professorService.inactivateProfessor(professorSelected.getIdUser())) {
            showAlert(Alert.AlertType.INFORMATION, "Exito", "Profesor inactivado correctamente");
            closeWindow(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo inactivar al profesor");
        }
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        closeWindow(event);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}