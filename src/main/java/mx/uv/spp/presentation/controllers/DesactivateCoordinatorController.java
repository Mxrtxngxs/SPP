package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
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
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.Optional;

public class DesactivateCoordinatorController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblPersonalNumber;

    private ICoordinatorService coordinatorService;
    private CoordinatorDTO coordinatorSelected;

    public DesactivateCoordinatorController() {
        try {
            this.coordinatorService = new CoordinatorServiceImplementation();
        } catch (DataAccessException e) {
            this.coordinatorService = null;
        }
    }

    @FXML
    public void initialize() {
        if (coordinatorService == null) {
            Platform.runLater(() -> showAlert("Error", "No hay conexion con la base de datos disponible."));
        }
    }

    public void setCoordinator(CoordinatorDTO coordinator) {
        this.coordinatorSelected = coordinator;
        if (coordinator != null) {
            lblName.setText(coordinator.getName());
            lblPersonalNumber.setText(coordinator.getStaffNumber());
        }
    }

    @FXML
    private void deactivateCoordinator(ActionEvent event) {
        if (isDataValid()) {
            if (showConfirmation()) {
                executeDeactivation(event);
            }
        }
    }

    private boolean isDataValid() {
        if (coordinatorService == null) {
            showAlert("Error", "No hay conexion con la base de datos.");
            return false;
        }

        if (coordinatorSelected == null || coordinatorSelected.getIdUser() <= 0) {
            showAlert("Error", "Informacion del coordinador no cargada.");
            return false;
        }
        return true;
    }

    private boolean showConfirmation() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmacion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Seguro que desea inactivar a este coordinador?");

        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void executeDeactivation(ActionEvent event) {
        try {
            if (coordinatorService.inactivateCoordinator(coordinatorSelected.getIdUser())) {
                showAlert("Exito", "Coordinador inactivado correctamente.");
                closeWindow(event);
            } else {
                showAlert("Error", "No se pudo inactivar al coordinador.");
            }
        } catch (DataAccessException e) {
            showAlert("Error", "Fallo la comunicacion con la base de datos.");
        }
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        closeWindow(event);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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