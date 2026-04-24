package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.service.ICoordinatorService;
import mx.uv.spp.business.service.implementations.CoordinatorServiceImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

public class RegisterCoordinatorController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtStaffNumber;

    @FXML
    private PasswordField txtPassword;

    private ICoordinatorService coordinatorService;

    public RegisterCoordinatorController() {
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

    @FXML
    private void registerCoordinator(ActionEvent event) {
        if (coordinatorService == null) {
            showAlert("Error", "No hay conexion con la base de datos.");
            return;
        }

        String name = txtName.getText().trim();
        String staffNumber = txtStaffNumber.getText().trim();
        String password = txtPassword.getText().trim();

        if (name.isEmpty() || staffNumber.isEmpty() || password.isEmpty()) {
            showAlert("Campos vacios", "Por favor, llene todos los campos.");
            return;
        }

        try {
            if (coordinatorService.existsStaffNumber(staffNumber)) {
                showAlert("Error", "El numero de personal ya esta registrado.");
                return;
            }

            CoordinatorDTO coordinator = new CoordinatorDTO();
            coordinator.setName(name);
            coordinator.setStaffNumber(staffNumber);
            coordinator.setPassword(password);
            coordinator.setState("Activo");

            if (coordinatorService.registerCoordinator(coordinator)) {
                showAlert("Exito", "Coordinador registrado correctamente.");
                clearFields();
            } else {
                showAlert("Error", "La contrasena no cumple con los requisitos de seguridad.");
            }
        } catch (DataAccessException e) {
            showAlert("Error", "Fallo la comunicacion con la base de datos.");
        }
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        clearFields();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtName.clear();
        txtStaffNumber.clear();
        txtPassword.clear();
    }
}