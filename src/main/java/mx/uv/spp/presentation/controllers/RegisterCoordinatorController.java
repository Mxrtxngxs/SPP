package mx.uv.spp.presentation.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.service.ICoordinatorService;
import mx.uv.spp.business.service.implementations.CoordinatorServiceImplementation;

public class RegisterCoordinatorController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtStaffNumber;

    @FXML
    private PasswordField txtPassword;

    private ICoordinatorService coordinatorService;

    public RegisterCoordinatorController() {
        this.coordinatorService = new CoordinatorServiceImplementation();
    }

    @FXML
    private void registerCoordinator(ActionEvent event) {
        String name = txtName.getText().trim();
        String staffNumber = txtStaffNumber.getText().trim();
        String password = txtPassword.getText().trim();

        if (validateFields(name, staffNumber, password)) {
            processRegistration(name, staffNumber, password);
        }
    }

    private boolean validateFields(String name, String staffNumber, String password) {
        boolean isValid = true;
        if (name.isEmpty() || staffNumber.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos vacios", "Por favor rellene todos los campos");
            isValid = false;
        }
        return isValid;
    }

    private void processRegistration(String name, String staffNumber, String password) {
        if (coordinatorService.existsStaffNumber(staffNumber)) {
            showAlert(Alert.AlertType.ERROR, "Error", "El numero de personal ya se encuentra registrado");
        } else {
            CoordinatorDTO coordinator = createCoordinatorDTO(name, staffNumber, password);

            if (coordinatorService.registerCoordinator(coordinator)) {
                showAlert(Alert.AlertType.INFORMATION, "Exito", "Se registro al coordinador exitosamente");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "La contrasena no cumple con los requisitos de seguridad (minimo 10 caracteres mayusculas minusculas y numeros)");
            }
        }
    }

    private CoordinatorDTO createCoordinatorDTO(String name, String staffNumber, String password) {
        CoordinatorDTO coordinator = new CoordinatorDTO();
        coordinator.setName(name);
        coordinator.setStaffNumber(staffNumber);
        coordinator.setPassword(password);
        coordinator.setState("Activo");
        return coordinator;
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        clearFields();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
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