package mx.uv.spp.presentation.controllers;

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
import mx.uv.spp.utils.LogConfig;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ListCoordinatorsController {

    @FXML
    private TableView<CoordinatorDTO> tvCoordinators;

    @FXML
    private TableColumn<CoordinatorDTO, String> colName;

    @FXML
    private TableColumn<CoordinatorDTO, String> colStaffNumber;

    private ICoordinatorService coordinatorService;
    private static final Logger LOG = LogConfig.getLogger(ListCoordinatorsController.class);

    public ListCoordinatorsController() {
        this.coordinatorService = new CoordinatorServiceImplementation();
    }

    @FXML
    public void initialize() {
        configureTable();
        loadData();
    }

    private void configureTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStaffNumber.setCellValueFactory(new PropertyValueFactory<>("staffNumber"));
    }

    private void loadData() {
        List<CoordinatorDTO> list = coordinatorService.getAllActiveCoordinators();
        ObservableList<CoordinatorDTO> coordinators = FXCollections.observableArrayList(list);
        tvCoordinators.setItems(coordinators);
    }

    @FXML
    private void openDesactivateWindow(ActionEvent event) {
        CoordinatorDTO selectedCoordinator = getSelectedCoordinator();

        if (selectedCoordinator != null) {
            showDeactivateView(selectedCoordinator);
            loadData();
        }
    }

    private CoordinatorDTO getSelectedCoordinator() {
        CoordinatorDTO selected = tvCoordinators.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Atencion", "Por favor seleccione un coordinador de la tabla");
        }
        return selected;
    }

    private void showDeactivateView(CoordinatorDTO coordinator) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mx/uv/spp/presentation/views/DesactivateCoordinatorView.fxml"));

            Parent root = loader.load();

            DesactivateCoordinatorController controller = loader.getController();
            controller.setCoordinator(coordinator);

            Stage stage = new Stage();
            stage.setTitle("Inactivar Coordinador");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            LOG.severe("Error loading UI: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Hubo un error al abrir la ventana de inactivacion");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) tvCoordinators.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}