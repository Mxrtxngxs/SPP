package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDAOImplementationTest {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImplementationTest.class.getName());
    private UserDAOImplementation userDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            userDAO = new UserDAOImplementation();
            cleanDatabase();
            insertBaseCoordinator(1, "Base Coordinator", "pass123", "Activo", "11111");
            insertBaseProfessor(2, "Base Professor", "pass123", "Activo", "22222", "Matutino");
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting up test database", e);
        }
    }

    @AfterEach
    public void tearDown() {
        cleanDatabase();
    }

    @Test
    public void saveCoordinator_validData_returnsTrue() throws DataAccessException {
        CoordinatorDTO coordinator = new CoordinatorDTO();
        coordinator.setName("New Coord");
        coordinator.setPassword("secure");
        coordinator.setStaffNumber("54321");

        boolean result = userDAO.saveCoordinator(coordinator);

        assertTrue(result);
    }

    @Test
    public void saveCoordinator_duplicateStaffNumber_throwsDataAccessException() {
        CoordinatorDTO duplicateCoordinator = new CoordinatorDTO();
        duplicateCoordinator.setName("Another Coord");
        duplicateCoordinator.setPassword("secure");
        duplicateCoordinator.setStaffNumber("11111"); // Ya existe por el setUp

        assertThrows(DataAccessException.class, () -> {
            userDAO.saveCoordinator(duplicateCoordinator);
        });
    }

    @Test
    public void saveProfessor_validData_returnsTrue() throws DataAccessException {
        ProfessorDTO professor = new ProfessorDTO();
        professor.setName("New Prof");
        professor.setPassword("secure");
        professor.setStaffNumber("65432");
        professor.setShift("Vespertino");

        boolean result = userDAO.saveProfessor(professor);

        assertTrue(result);
    }

    @Test
    public void saveIntern_validData_returnsTrue() throws DataAccessException {
        InternDTO intern = new InternDTO();
        intern.setName("New Intern");
        intern.setPassword("secure");
        intern.setEnrollmentNumber("S12345678");
        intern.setGender("M");
        intern.setSpeaksIndigenousLanguage(false);
        intern.setIndigenousLanguage(null);
        intern.setCoordinatorId(1);
        intern.setProfessorId(2);

        boolean result = userDAO.saveIntern(intern);

        assertTrue(result);
    }

    @Test
    public void saveIntern_missingForeignKeys_throwsDataAccessException() {
        InternDTO intern = new InternDTO();
        intern.setName("Invalid Intern");
        intern.setPassword("secure");
        intern.setEnrollmentNumber("S99999999");
        intern.setGender("F");
        intern.setSpeaksIndigenousLanguage(false);
        intern.setCoordinatorId(999); // No existe
        intern.setProfessorId(999); // No existe

        assertThrows(DataAccessException.class, () -> {
            userDAO.saveIntern(intern);
        });
    }

    @Test
    public void inactivateUser_existingActiveId_returnsTrue() throws DataAccessException {
        boolean result = userDAO.inactivateUser(1);

        assertTrue(result);
    }

    @Test
    public void inactivateUser_nonExistentId_returnsFalse() throws DataAccessException {
        boolean result = userDAO.inactivateUser(999);

        assertFalse(result);
    }

    @Test
    public void authenticateUser_validCoordinatorCredentials_returnsUserDTO() throws DataAccessException {
        UserDTO result = userDAO.authenticateUser("11111", "pass123");

        assertTrue(result.getIdUser() > 0);
        assertEquals("Coordinador", result.getRole());
        assertEquals("Activo", result.getState());
    }

    @Test
    public void authenticateUser_validProfessorCredentials_returnsUserDTO() throws DataAccessException {
        UserDTO result = userDAO.authenticateUser("22222", "pass123");

        assertTrue(result.getIdUser() > 0);
        assertEquals("Profesor", result.getRole());
        assertEquals("Activo", result.getState());
    }

    @Test
    public void authenticateUser_invalidPassword_returnsFlagUser() throws DataAccessException {
        UserDTO result = userDAO.authenticateUser("11111", "wrongpass");

        assertEquals(-1, result.getIdUser());
    }

    @Test
    public void authenticateUser_nonExistentIdentifier_returnsFlagUser() throws DataAccessException {
        UserDTO result = userDAO.authenticateUser("00000", "pass123");

        assertEquals(-1, result.getIdUser());
    }

    @Test
    public void authenticateUser_inactiveUser_returnsFlagUser() throws DataAccessException {
        userDAO.inactivateUser(1); // Inactivamos al coordinador

        UserDTO result = userDAO.authenticateUser("11111", "pass123");

        assertEquals(-1, result.getIdUser());
    }

    private void insertBaseCoordinator(int id, String name, String pass, String state, String staffNum) {
        String insertUser = "INSERT INTO Usuario (id_usuario, nombre, contrasena, estado, rol) VALUES (?, ?, ?, ?, 'Coordinador')";
        String insertDetail = "INSERT INTO Coordinador_Detalle (id_usuario, numero_personal) VALUES (?, ?)";
        try (PreparedStatement stUser = connection.prepareStatement(insertUser);
             PreparedStatement stDetail = connection.prepareStatement(insertDetail)) {

            stUser.setInt(1, id);
            stUser.setString(2, name);
            stUser.setString(3, pass);
            stUser.setString(4, state);
            stUser.executeUpdate();

            stDetail.setInt(1, id);
            stDetail.setString(2, staffNum);
            stDetail.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting base coordinator", e);
        }
    }

    private void insertBaseProfessor(int id, String name, String pass, String state, String staffNum, String shift) {
        String insertUser = "INSERT INTO Usuario (id_usuario, nombre, contrasena, estado, rol) VALUES (?, ?, ?, ?, 'Profesor')";
        String insertDetail = "INSERT INTO Profesor_Detalle (id_usuario, numero_personal, turno) VALUES (?, ?, ?)";
        try (PreparedStatement stUser = connection.prepareStatement(insertUser);
             PreparedStatement stDetail = connection.prepareStatement(insertDetail)) {

            stUser.setInt(1, id);
            stUser.setString(2, name);
            stUser.setString(3, pass);
            stUser.setString(4, state);
            stUser.executeUpdate();

            stDetail.setInt(1, id);
            stDetail.setString(2, staffNum);
            stDetail.setString(3, shift);
            stDetail.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting base professor", e);
        }
    }

    private void cleanDatabase() {
        try {
            String[] cleanQueries = {
                    "DELETE FROM Practicante_Detalle",
                    "DELETE FROM Profesor_Detalle",
                    "DELETE FROM Coordinador_Detalle",
                    "DELETE FROM Usuario"
            };

            for (String query : cleanQueries) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up database tables", e);
        }
    }
}