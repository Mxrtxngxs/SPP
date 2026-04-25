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

    private IAuthenticationService authenticationService;

    public LoginController() {
        try {
            this.authenticationService = new AuthenticationServiceImplementation();
        } catch (DataAccessException e) {
            this.authenticationService = null;
        }
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
        try {
            UserDTO user = authenticationService.login(username, password);

            if (user.getIdUser() != -1) {
                showAlert("Exito", "Bienvenido: " + user.getName());
            } else {
                lblErrorMessage.setText("Correo o contraseña incorrecta");
            }
        } catch (DataAccessException e) {
            showAlert("Error", "Error con el servidor");
        }
    }

    private boolean validateFields(String username, String password){
        if (username.isEmpty() || password.isEmpty()) {
            lblErrorMessage.setText("Por favor ingresa usuario y contraseña");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}