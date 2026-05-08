package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.service.IProfessorService;
import mx.uv.spp.business.service.implementations.ProfessorServiceImplementation;

import java.util.Optional;

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
        this.professorService = new ProfessorServiceImplementation();
    }

    @FXML
    public void initialize() {
        if (professorService == null) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Servicio no disponible"));
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
        boolean isValid = true;
        if (name.isEmpty() || staffNumber.isEmpty() || password.isEmpty() || shift == null) {
            showAlert(Alert.AlertType.WARNING, "Campos vacios", "Por favor, rellene todos los campos y seleccione un turno");
            isValid = false;
        }
        return isValid;
    }

    private void processRegistration(String name, String staffNumber, String password, String shift) {
        if (professorService != null) {
            if (professorService.existsStaffNumber(staffNumber)) {
                showAlert(Alert.AlertType.ERROR, "Error", "El numero de personal ya se encuentra registrado");
            } else {
                ProfessorDTO professor = createProfessorDTO(name, staffNumber, password, shift);

                if (professorService.registerProfessor(professor)) {
                    showAlert(Alert.AlertType.INFORMATION, "Exito", "El profesor se registro exitosamente");
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "La contrasena no cumple con los requisitos de seguridad (minimo 10 caracteres, mayusculas, minusculas y numeros)");
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Servicio no disponible");
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
    private void returnMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/spp/presentation/views/admin/AdministratorMenuView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Menu Coordinador");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana");
        }
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cancelacion");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea cancelar el registro y regresar al menu?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnMenu(event);
        }
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
        cbShift.getSelectionModel().clearSelection();
    }
}