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

    public LoginController() {
        this.authenticationService = new AuthenticationServiceImplementation();
    }

    @FXML
    public void initialize() {
        if (authenticationService == null) {
            Platform.runLater(() -> showAlert("Error", "Hubo un error al conectarse a la base de datos"));
        }
    }

    @FXML
    private void login(ActionEvent event) {
        lblErrorMessage.setText("");

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if(validateFields(username, password)){
            authenticateUser(username, password);
        }
    }

    private void authenticateUser(String username, String password){
        UserDTO user = authenticationService.login(username, password);

        if (user != null && user.getIdUser() != -1) {
            showAlert("Exito", "Bienvenido: " + user.getName());
        } else {
            lblErrorMessage.setText("Correo o contraseña incorrecta");
        }
    }

    private boolean validateFields(String username, String password){
        boolean isValid = true;
        if (username.isEmpty() || password.isEmpty()) {
            lblErrorMessage.setText("Por favor ingresa usuario y contraseña");
            isValid = false;
        }
        return isValid;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}