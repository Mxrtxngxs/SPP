package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.business.service.IAdministratorService;
import mx.uv.spp.business.service.implementations.AdministratorServiceImplementation;

import java.io.IOException;

public class RegisterAdministratorController {

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    private IAdministratorService adminService;

    public RegisterAdministratorController() {
        this.adminService = new AdministratorServiceImplementation();
    }

    @FXML
    public void initialize() {
        if (adminService == null) {
            Platform.runLater(() -> showAlert("Error", "Servicio de administrador no disponible"));
        }
    }

    @FXML
    private void registerAdmin(ActionEvent event) {
        String name = txtName.getText().trim();
        String password = txtPassword.getText().trim();

        if (validateFields(name, password)) {
            UserDTO admin = new UserDTO();
            admin.setName(name);
            admin.setPassword(password);

            if (adminService.registerAdmin(admin)) {
                showAlert("Éxito", "Administrador registrado correctamente. Por favor inicie sesión.");
                returnToLogin();
            } else {
                showAlert("Error", "La contraseña no cumple con los requisitos (mínimo 10 caracteres, mayúsculas, minúsculas y números) o hubo un error en la BD.");
            }
        }
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        returnToLogin();
    }

    private boolean validateFields(String name, String password) {
        boolean isValid = true;
        if (name.isEmpty() || password.isEmpty()) {
            showAlert("Campos vacíos", "Por favor, rellene todos los campos.");
            isValid = false;
        }
        return isValid;
    }

    private void returnToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/spp/presentation/views/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Inicio de Sesión - SPP");
            stage.setScene(new Scene(root));
            stage.show();

            closeCurrentWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar la ventana de inicio de sesión");
        }
    }

    private void closeCurrentWindow() {
        Stage currentStage = (Stage) txtName.getScene().getWindow();
        if (currentStage != null) {
            currentStage.close();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}