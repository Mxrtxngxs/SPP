package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.business.service.IAdministratorService;
import mx.uv.spp.business.service.IAuthenticationService;
import mx.uv.spp.business.service.implementations.AdministratorServiceImplementation;
import mx.uv.spp.business.service.implementations.AuthenticationServiceImplementation;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private Button btnLogin;

    private IAuthenticationService authenticationService;
    private IAdministratorService adminService;

    public LoginController() {
        this.authenticationService = new AuthenticationServiceImplementation();
        this.adminService = new AdministratorServiceImplementation();
    }

    @FXML
    public void initialize() {
        if (authenticationService != null && adminService != null) {
            limitTextFieldLength(txtUsername, 50);
            limitTextFieldLength(txtPassword, 255);

            Platform.runLater(() -> {
                if (!adminService.existsAdmin()) {
                    redirectToAdminRegistration();
                }
            });
        } else {
            Platform.runLater(() -> showAlert("Error", "Servicios de autenticación no disponibles"));
        }
    }

    @FXML
    private void login(ActionEvent event) {
        lblErrorMessage.setText("");
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (validateFields(username, password)) {
            authenticateUser(username, password);
        }
    }

    private void authenticateUser(String username, String password) {
        UserDTO user = authenticationService.login(username, password);

        if (user != null && user.getIdUser() != -1) {
            redirectBasedOnRole(user);
        } else {
            lblErrorMessage.setText("Usuario o contraseña incorrecta");
        }
    }

    private void redirectBasedOnRole(UserDTO user) {
        String fxmlPath = "";
        String title = "";
        boolean isRoleValid = true;

        switch (user.getRole()) {
            case "Administrador":
                fxmlPath = "/mx/uv/spp/presentation/views/AdministratorMenuView.fxml";
                title = "Menu Administrador";
                break;
            case "Coordinador":
                fxmlPath = "/mx/uv/spp/presentation/views/CoordinatorMenuView.fxml";
                title = "Menu Coordinador";
                break;
            case "Profesor":
                fxmlPath = "/mx/uv/spp/presentation/views/ProfessorMenuView.fxml";
                title = "Menu Profesor";
                break;
            case "Practicante":
                fxmlPath = "/mx/uv/spp/presentation/views/InternMenuView.fxml";
                title = "Menu Practicante";
                break;
            default:
                lblErrorMessage.setText("Rol no reconocido: " + user.getRole());
                isRoleValid = false;
                break;
        }

        if (isRoleValid) {
            loadDashboard(fxmlPath, title, "Bienvenido: " + user.getName());
        }
    }

    private void loadDashboard(String fxmlPath, String title, String welcomeMessage) {
        URL fxmlLocation = getClass().getResource(fxmlPath);

        if (fxmlLocation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle(title);
                stage.setScene(new Scene(root));
                stage.show();

                showAlert("exito", welcomeMessage);
                closeWindow();
            } catch (IOException e) {
                showAlert("Error", "Error al cargar la vista principal");
            }
        } else {
            showAlert("Error de ubicacion", "No se encontro el archivo: " + fxmlPath);
        }
    }

    private boolean validateFields(String username, String password) {
        boolean isValid = true;
        if (username.isEmpty() || password.isEmpty()) {
            lblErrorMessage.setText("Por favor ingresa usuario y contraseña");
            isValid = false;
        }
        return isValid;
    }

    private void redirectToAdminRegistration() {
        loadDashboard("/mx/uv/spp/presentation/views/RegisterAdministratorView.fxml",
                "Registrar Administrador",
                "No existe un administrador. Por favor registrese");
    }

    private void closeWindow() {
        if (btnLogin.getScene() != null) {
            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
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

    private void limitTextFieldLength(TextField textField, int maxLength) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            TextFormatter.Change result = null;
            if (change.getControlNewText().length() <= maxLength) {
                result = change;
            }
            return result;
        }));
    }
}