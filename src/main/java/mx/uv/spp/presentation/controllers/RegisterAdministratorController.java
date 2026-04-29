package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.business.service.IAdministratorService;
import mx.uv.spp.business.service.implementations.AdministratorServiceImplementation;
import mx.uv.spp.utils.LogConfig;

import java.io.IOException;
import java.util.logging.Logger;

public class RegisterAdministratorController {

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    private IAdministratorService adminService;

    private static final Logger LOG = LogConfig.getLogger(RegisterAdministratorController.class);

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
                showAlert("Exito", "Administrador registrado correctamente. Por favor inicie sesión.");
                returnToLogin(event);
            } else {
                showAlert("Error", "La contraseña no cumple con los requisitos (mínimo 10 caracteres, mayusculas, minusculas y numeros)");
            }
        }
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        returnToLogin(event);
    }

    private boolean validateFields(String name, String password) {
        boolean isValid = true;
        if (name.isEmpty() || password.isEmpty()) {
            showAlert("Campos vacíos", "Por favor, rellene todos los campos.");
            isValid = false;
        }
        return isValid;
    }

    @FXML
    private void returnMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/spp/presentation/views/CoordinatorMenuView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Menu Coordinador");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            LOG.severe("Error loading UI: " + e.getMessage());
            showAlert("Error", "No se pudo cargar la ventana");
        }
    }

    private void returnToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/spp/presentation/views/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Inicio de Sesión - SPP");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            LOG.severe("Error loading UI: " + e.getMessage());
            showAlert("Error", "No se pudo cargar la ventana de inicio de sesion");
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