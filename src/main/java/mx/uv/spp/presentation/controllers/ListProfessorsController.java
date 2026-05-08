package mx.uv.spp.presentation.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.service.IProfessorService;
import mx.uv.spp.business.service.implementations.ProfessorServiceImplementation;

import java.util.List;

public class ListProfessorsController {

    @FXML
    private Label lblCurrentUser;

    @FXML
    private TableView<ProfessorDTO> tvProfessors;

    @FXML
    private TableColumn<ProfessorDTO, String> colName;

    @FXML
    private TableColumn<ProfessorDTO, String> colStaffNumber;

    @FXML
    private TableColumn<ProfessorDTO, String> colShift;

    @FXML
    private TableColumn<ProfessorDTO, String> colStatus;

    private IProfessorService professorService;

    public ListProfessorsController() {
        this.professorService = new ProfessorServiceImplementation();
    }

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStaffNumber.setCellValueFactory(new PropertyValueFactory<>("staffNumber"));
        colShift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("state"));
        loadProfessors();
    }

    private void loadProfessors() {
        List<ProfessorDTO> professors = professorService.getActiveProfessors();

        if (professors == null || professors.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Sin registros", "No existe ningun profesor registrado.");
            goBack(null);
            return;
        }

        ObservableList<ProfessorDTO> list = FXCollections.observableArrayList(professors);
        tvProfessors.setItems(list);
    }

    @FXML
    private void openDesactivateWindow(ActionEvent event) {
        ProfessorDTO selected = tvProfessors.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Seleccion requerida", "Selecciona un profesor de la lista.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/mx/uv/spp/presentation/views/admin/DesactivateProfessorView.fxml"));
            Parent root = loader.load();

            DesactivateProfessorController controller = loader.getController();
            controller.setProfessor(selected);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Inactivar Profesor");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana. Intente mas tarde.");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/mx/uv/spp/presentation/views/admin/AdministratorMenuView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tvProfessors.getScene().getWindow();
            stage.setTitle("Menu Administrador");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentUser(String username) {
        lblCurrentUser.setText(username);
    }
}