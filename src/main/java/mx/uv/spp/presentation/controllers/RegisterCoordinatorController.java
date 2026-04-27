package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
            Platform.runLater(() -> showAlert("Error", "Hubo un error al conectarse a la base de datos"));
        }
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
        if (name.isEmpty() || staffNumber.isEmpty() || password.isEmpty()) {
            showAlert("Campos vacios", "Por favor, rellene todos los campos");
            return false;
        }
        return true;
    }

    private void processRegistration(String name, String staffNumber, String password) {

        try {
            if (coordinatorService.existsStaffNumber(staffNumber)) {
                showAlert("Error", "El numero de personal ya se encuentra registrado");
                return;
            }

            CoordinatorDTO coordinator = createCoordinatorDTO(name, staffNumber, password);

            if (coordinatorService.registerCoordinator(coordinator)) {
                showAlert("Exito", "Se registro al coordinador exitosamente");
                clearFields();
            } else {
                showAlert("Error", "La contraseña no cumple con los requisitos de seguridad (minimo 10 caracteres, mayusculas, minusculas y numeros)");
            }
        } catch (DataAccessException e) {
            showAlert("Error", "Fallo la comunicacion con la base de datos");
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