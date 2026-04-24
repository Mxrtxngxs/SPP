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

public class DeactivateCoordinatorController {

    @FXML
    private Label lblName;

    @FXML
    private Label lblPersonalNumber;

    private ICoordinatorService coordinatorService;
    private CoordinatorDTO coordinatorSelected;

    public DeactivateCoordinatorController() {
        try {
            this.coordinatorService = new CoordinatorServiceImplementation();
        } catch (DataAccessException e) {
            this.coordinatorService = null;
        }
    }

    @FXML
    public void initialize() {
        if (coordinatorService == null) {
            Platform.runLater(() -> showAlert("Error", "No hay conexion con la base de datos."));
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
        if (coordinatorService == null) {
            showAlert("Error", "No hay conexion con la base de datos.");
            return;
        }

        if (coordinatorSelected == null || coordinatorSelected.getIdUser() <= 0) {
            showAlert("Error", "No se ha cargado la informacion del coordinador.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmacion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Seguro que desea inactivar a este Coordinador?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (coordinatorService.inactivateCoordinator(coordinatorSelected.getIdUser())) {
                    showAlert("Exito", "Coordinador inactivado correctamente.");
                    closeWindow(event);
                } else {
                    showAlert("Error", "No se pudo inactivar al coordinador.");
                }
            } catch (DataAccessException e) {
                showAlert("Error", "Fallo la comunicacion con la base de datos al intentar inactivar.");
            }
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