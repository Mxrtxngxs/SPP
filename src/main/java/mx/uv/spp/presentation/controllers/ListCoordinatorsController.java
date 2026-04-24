package mx.uv.spp.presentation.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.service.ICoordinatorService;
import mx.uv.spp.business.service.implementations.CoordinatorServiceImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.io.IOException;
import java.util.List;

public class ListCoordinatorsController {

    @FXML
    private TableView<CoordinatorDTO> tvCoordinators;

    @FXML
    private TableColumn<CoordinatorDTO, String> colName;

    @FXML
    private TableColumn<CoordinatorDTO, String> colStaffNumber;

    private ICoordinatorService coordinatorService;

    public ListCoordinatorsController() {
        try {
            this.coordinatorService = new CoordinatorServiceImplementation();
        } catch (DataAccessException e) {
            this.coordinatorService = null;
        }
    }

    @FXML
    public void initialize() {
        if (coordinatorService == null) {
            Platform.runLater(() -> showAlert("Error", "No hay conexion con la base de datos."));
        } else {
            configureTable();
            loadData();
        }
    }

    private void configureTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStaffNumber.setCellValueFactory(new PropertyValueFactory<>("staffNumber"));
    }

    private void loadData() {
        if (coordinatorService != null) {
            try {
                List<CoordinatorDTO> list = coordinatorService.getAllCoordinators();
                ObservableList<CoordinatorDTO> coordinators = FXCollections.observableArrayList(list);
                tvCoordinators.setItems(coordinators);
            } catch (DataAccessException e) {
                showAlert("Error", "No se pudieron cargar los coordinadores.");
            }
        }
    }

    @FXML
    private void openDeactivateWindow(ActionEvent event) {
        if (coordinatorService == null) {
            showAlert("Error", "No hay conexion con la base de datos.");
            return;
        }

        CoordinatorDTO selectedCoordinator = tvCoordinators.getSelectionModel().getSelectedItem();

        if (selectedCoordinator == null) {
            showAlert("Atencion", "Por favor, seleccione un coordinador de la tabla.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/spp/presentation/views/DesactivateCoordinatorView.fxml"));
            Parent root = loader.load();

            DeactivateCoordinatorController controller = loader.getController();
            controller.setCoordinator(selectedCoordinator);

            Stage stage = new Stage();
            stage.setTitle("Inactivar Coordinador");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadData();

        } catch (IOException e) {
            showAlert("Error", "No se pudo abrir la ventana de inactivacion");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) tvCoordinators.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}