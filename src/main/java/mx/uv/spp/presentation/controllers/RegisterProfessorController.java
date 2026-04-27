package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.service.IProfessorService;
import mx.uv.spp.business.service.implementations.ProfessorServiceImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

public class RegisterProfessorController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtStaffNumber;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cbShift;

    private IProfessorService professorService;

    public RegisterProfessorController() {
        try {
            this.professorService = new ProfessorServiceImplementation();
        } catch (DataAccessException e) {
            this.professorService = null;
        }
    }

    @FXML
    public void initialize() {
        if (professorService == null) {
            Platform.runLater(() -> showAlert("Error", "Hubo un error al conectarse a la base de datos"));
        } else {
            ObservableList<String> shifts = FXCollections.observableArrayList("Matutino", "Vespertino");
            cbShift.setItems(shifts);
        }
    }

    @FXML
    private void registerProfessor(ActionEvent event) {
        String name = txtName.getText().trim();
        String staffNumber = txtStaffNumber.getText().trim();
        String password = txtPassword.getText().trim();
        String shift = cbShift.getValue();

        if (validateFields(name, staffNumber, password, shift)) {
            processRegistration(name, staffNumber, password, shift);
        }
    }

    private boolean validateFields(String name, String staffNumber, String password, String shift) {
        if (name.isEmpty() || staffNumber.isEmpty() || password.isEmpty() || shift == null) {
            showAlert("Campos vacios", "Por favor, rellene todos los campos y seleccione un turno");
            return false;
        }
        return true;
    }

    private void processRegistration(String name, String staffNumber, String password, String shift) {
        if (professorService == null) {
            showAlert("Error", "Hubo un error al conectarse a la base de datos");
            return;
        }

        try {
            if (professorService.existsStaffNumber(staffNumber)) {
                showAlert("Error", "El numero de personal ya se encuentra registrado");
                return;
            }

            ProfessorDTO professor = createProfessorDTO(name, staffNumber, password, shift);

            if (professorService.registerProfessor(professor)) {
                showAlert("Exito", "El profesor se registro exitosamente");
                clearFields();
            } else {
                showAlert("Error", "La contrasena no cumple con los requisitos de seguridad (minimo 10 caracteres, mayusculas, minusculas y numeros)");
            }
        } catch (DataAccessException e) {
            showAlert("Error", "Fallo en la base de datos");
        }
    }

    private ProfessorDTO createProfessorDTO(String name, String staffNumber, String password, String shift) {
        ProfessorDTO professor = new ProfessorDTO();
        professor.setName(name);
        professor.setStaffNumber(staffNumber);
        professor.setPassword(password);
        professor.setShift(shift);
        professor.setState("Activo");
        return professor;
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
        cbShift.getSelectionModel().clearSelection();
    }
}