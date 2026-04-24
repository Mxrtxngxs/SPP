package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.business.service.IAuthenticationService;
import mx.uv.spp.business.service.implementations.AuthenticationServiceImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private Button btnLogin;

    private IAuthenticationService authService;

    public LoginController() {
        try {
            this.authService = new AuthenticationServiceImplementation();
        } catch (DataAccessException e) {
            this.authService = null;
        }
    }

    @FXML
    public void initialize() {
        if (authService == null) {
            Platform.runLater(() -> showAlert("Error", "No hay conexion con la base de datos."));
        }
    }

    @FXML
    private void login(ActionEvent event) {
        lblErrorMessage.setText("");

        if (authService == null) {
            showAlert("Error", "No hay conexion con la base de datos.");
            return;
        }

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblErrorMessage.setText("Por favor, ingresa usuario y contrasena.");
            return;
        }

        try {
            UserDTO user = authService.login(username, password);

            if (user.getIdUser() != -1) {
                showAlert("Exito", "Inicio de sesion exitoso. Bienvenido: " + user.getName());
            } else {
                lblErrorMessage.setText("Usuario o contrasena incorrectos.");
            }
        } catch (DataAccessException e) {
            showAlert("Error", "Error con el servidor.");
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