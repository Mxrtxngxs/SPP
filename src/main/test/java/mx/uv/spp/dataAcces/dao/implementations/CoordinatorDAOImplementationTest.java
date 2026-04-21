package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinatorDAOImplementationTest {

    private static final Logger LOGGER = Logger.getLogger(CoordinatorDAOImplementationTest.class.getName());
    private CoordinatorDAOImplementation coordinatorDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            coordinatorDAO = new CoordinatorDAOImplementation();

            cleanDatabase();
            insertBaseCoordinator(1, "Base Coordinator", "pass123", "Activo", "12345");
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting up test database", e);
        }
    }

    @AfterEach
    public void tearDown() {
        cleanDatabase();
    }

    // --- existsStaffNumber Tests ---

    @Test
    public void existsStaffNumber_existingNumber_returnsTrue() throws DataAccessException {
        boolean result = coordinatorDAO.existsStaffNumber("12345");

        assertTrue(result);
    }

    @Test
    public void existsStaffNumber_nonExistingNumber_returnsFalse() throws DataAccessException {
        boolean result = coordinatorDAO.existsStaffNumber("99999");

        assertFalse(result);
    }

    @Test
    public void existsStaffNumber_nullNumber_returnsFalse() throws DataAccessException {
        boolean result = coordinatorDAO.existsStaffNumber(null);

        assertFalse(result);
    }

    // --- inactivateCurrentCoordinators Tests ---

    @Test
    public void inactivateCurrentCoordinators_existingActiveCoordinators_returnsTrue() throws DataAccessException {
        boolean result = coordinatorDAO.inactivateCurrentCoordinators();

        assertTrue(result);
        CoordinatorDTO updatedCoordinator = coordinatorDAO.getCoordinatorById(1);
        assertEquals("No Activo", updatedCoordinator.getState());
    }

    @Test
    public void inactivateCurrentCoordinators_noCoordinators_returnsTrue() throws DataAccessException {
        cleanDatabase();

        boolean result = coordinatorDAO.inactivateCurrentCoordinators();

        assertTrue(result);
    }

    @Test
    public void inactivateCurrentCoordinators_alreadyInactiveCoordinators_returnsTrue() throws DataAccessException {
        coordinatorDAO.inactivateCurrentCoordinators();

        boolean result = coordinatorDAO.inactivateCurrentCoordinators();

        assertTrue(result);
    }

    // --- saveCoordinator Tests ---

    @Test
    public void saveCoordinator_validData_returnsTrue() throws DataAccessException {
        CoordinatorDTO newCoordinator = createBaseCoordinatorDTO("New Coord", "54321");

        boolean result = coordinatorDAO.saveCoordinator(newCoordinator);

        assertTrue(result);
    }

    @Test
    public void saveCoordinator_duplicateStaffNumber_throwsDataAccessException() {
        CoordinatorDTO duplicateCoordinator = createBaseCoordinatorDTO("Another Coord", "12345");

        assertThrows(DataAccessException.class, () -> {
            coordinatorDAO.saveCoordinator(duplicateCoordinator);
        });
    }

    @Test
    public void saveCoordinator_nullName_throwsDataAccessException() {
        CoordinatorDTO invalidCoordinator = createBaseCoordinatorDTO(null, "67890");

        assertThrows(DataAccessException.class, () -> {
            coordinatorDAO.saveCoordinator(invalidCoordinator);
        });
    }

    @Test
    public void saveCoordinator_nullPassword_throwsDataAccessException() {
        CoordinatorDTO invalidCoordinator = createBaseCoordinatorDTO("Coord Name", "67890");
        invalidCoordinator.setPassword(null);

        assertThrows(DataAccessException.class, () -> {
            coordinatorDAO.saveCoordinator(invalidCoordinator);
        });
    }

    // --- inactivateCoordinator Tests ---

    @Test
    public void inactivateCoordinator_existingActiveId_returnsTrue() throws DataAccessException {
        boolean result = coordinatorDAO.inactivateCoordinator(1);

        assertTrue(result);
        CoordinatorDTO updatedCoordinator = coordinatorDAO.getCoordinatorById(1);
        assertEquals("No Activo", updatedCoordinator.getState());
    }

    @Test
    public void inactivateCoordinator_nonExistentId_returnsFalse() throws DataAccessException {
        boolean result = coordinatorDAO.inactivateCoordinator(999);

        assertFalse(result);
    }

    @Test
    public void inactivateCoordinator_negativeId_returnsFalse() throws DataAccessException {
        boolean result = coordinatorDAO.inactivateCoordinator(-1);

        assertFalse(result);
    }

    // --- getCoordinatorById Tests ---

    @Test
    public void getCoordinatorById_existingId_returnsCoordinator() throws DataAccessException {
        CoordinatorDTO result = coordinatorDAO.getCoordinatorById(1);

        assertNotNull(result);
        assertEquals("12345", result.getStaffNumber());
    }

    @Test
    public void getCoordinatorById_nonExistentId_returnsNull() throws DataAccessException {
        CoordinatorDTO result = coordinatorDAO.getCoordinatorById(999);

        assertNull(result);
    }

    @Test
    public void getCoordinatorById_negativeId_returnsNull() throws DataAccessException {
        CoordinatorDTO result = coordinatorDAO.getCoordinatorById(-1);

        assertNull(result);
    }

    // --- getAllCoordinators Tests ---

    @Test
    public void getAllCoordinators_existingCoordinators_returnsList() throws DataAccessException {
        List<CoordinatorDTO> result = coordinatorDAO.getAllCoordinators();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void getAllCoordinators_noCoordinators_returnsEmptyList() throws DataAccessException {
        cleanDatabase();

        List<CoordinatorDTO> result = coordinatorDAO.getAllCoordinators();

        assertTrue(result.isEmpty());
    }

    @Test
    public void getAllCoordinators_multipleCoordinators_returnsList() throws DataAccessException {
        CoordinatorDTO secondCoordinator = createBaseCoordinatorDTO("Second Coord", "54321");
        coordinatorDAO.saveCoordinator(secondCoordinator);

        List<CoordinatorDTO> result = coordinatorDAO.getAllCoordinators();

        assertEquals(2, result.size());
    }

    // --- Private Helper Methods ---

    private CoordinatorDTO createBaseCoordinatorDTO(String name, String staffNumber) {
        CoordinatorDTO coordinator = new CoordinatorDTO();
        coordinator.setName(name);
        coordinator.setPassword("securePass");
        coordinator.setState("Activo");
        coordinator.setStaffNumber(staffNumber);
        return coordinator;
    }

    private void insertBaseCoordinator(int id, String name, String pass, String state, String staffNum) {
        String insertUser = "INSERT INTO Usuario (id_usuario, nombre, contrasena, estado, rol) VALUES (?, ?, ?, ?, 'Coordinador')";
        try (PreparedStatement st = connection.prepareStatement(insertUser)) {
            st.setInt(1, id);
            st.setString(2, name);
            st.setString(3, pass);
            st.setString(4, state);
            st.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting base user", e);
        }

        String insertDetail = "INSERT INTO Coordinador_Detalle (id_usuario, numero_personal) VALUES (?, ?)";
        try (PreparedStatement st = connection.prepareStatement(insertDetail)) {
            st.setInt(1, id);
            st.setString(2, staffNum);
            st.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting base coordinator detail", e);
        }
    }

    private void cleanDatabase() {
        try {
            String cleanDetail = "DELETE FROM Coordinador_Detalle";
            try (PreparedStatement statement = connection.prepareStatement(cleanDetail)) {
                statement.executeUpdate();
            }

            String cleanUsers = "DELETE FROM Usuario";
            try (PreparedStatement statement = connection.prepareStatement(cleanUsers)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up database tables", e);
        }
    }
}