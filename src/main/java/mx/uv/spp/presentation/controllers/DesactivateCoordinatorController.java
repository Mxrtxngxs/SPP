package mx.uv.spp.presentation.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.service.ICoordinatorService;
import mx.uv.spp.business.service.implementations.CoordinatorServiceImplementation;

public class DesactivateCoordinatorController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblPersonalNumber;

    @FXML
    private Label lblState;

    private ICoordinatorService coordinatorService;
    private CoordinatorDTO coordinatorSelected;

    public DesactivateCoordinatorController() {
        this.coordinatorService = new CoordinatorServiceImplementation();
    }

    public void setCoordinator(CoordinatorDTO coordinator) {
        this.coordinatorSelected = coordinator;
        if (coordinator != null) {
            lblName.setText(coordinator.getName());
            lblPersonalNumber.setText(coordinator.getStaffNumber());
            lblState.setText(coordinator.getState());
        }
    }

    @FXML
    private void desactivateCoordinator(ActionEvent event) {
        if (isDataValid() && showConfirmation()) {
            executeDeactivation(event);
        }
    }

    private boolean isDataValid() {
        boolean isValid = true;
        if (coordinatorService == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Servicio no disponible");
            isValid = false;
        } else if (coordinatorSelected == null || coordinatorSelected.getIdUser() <= 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Informacion del coordinador no cargada");
            isValid = false;
        }
        return isValid;
    }

    private boolean showConfirmation() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmacion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Seguro que desea inactivar a este coordinador");

        return confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void executeDeactivation(ActionEvent event) {
        if (coordinatorService.inactivateCoordinator(coordinatorSelected.getIdUser())) {
            showAlert(Alert.AlertType.INFORMATION, "Exito", "Coordinador inactivado correctamente");
            closeWindow(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo inactivar al coordinador");
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