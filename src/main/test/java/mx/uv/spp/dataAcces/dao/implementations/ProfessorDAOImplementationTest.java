package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfessorDAOImplementationTest {

    private static final Logger LOGGER = Logger.getLogger(ProfessorDAOImplementationTest.class.getName());
    private ProfessorDAOImplementation professorDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            professorDAO = new ProfessorDAOImplementation();
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting up test database", e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            if (connection != null && !connection.isClosed()) {
                String cleanProfessorDetail = "DELETE FROM Profesor_Detalle";
                try (PreparedStatement statement = connection.prepareStatement(cleanProfessorDetail)) {
                    statement.executeUpdate();
                }

                String cleanUsers = "DELETE FROM Usuario WHERE rol = 'Profesor'";
                try (PreparedStatement statement = connection.prepareStatement(cleanUsers)) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up test database", e);
        }
    }

    @Test
    public void saveProfessor_NormalFlow_ReturnsTrue() throws DataAccessException {
        ProfessorDTO professor = createBaseProfessor("12345");
        boolean result = professorDAO.saveProfessor(professor);
        assertTrue(result);
    }

    @Test
    public void saveProfessor_AlternateFlow_DuplicateStaffNumber_ThrowsException() throws DataAccessException {
        ProfessorDTO professor1 = createBaseProfessor("99999");
        professorDAO.saveProfessor(professor1);

        ProfessorDTO professor2 = createBaseProfessor("99999");
        assertThrows(DataAccessException.class, () -> {
            professorDAO.saveProfessor(professor2);
        });
    }

    @Test
    public void saveProfessor_AlternateFlow_MissingRequiredData_ThrowsException() {
        ProfessorDTO professor = createBaseProfessor("11111");
        professor.setName(null);

        assertThrows(DataAccessException.class, () -> {
            professorDAO.saveProfessor(professor);
        });
    }

    @Test
    public void saveProfessor_ExceptionFlow_InvalidEnumShift_ThrowsException() {
        ProfessorDTO professor = createBaseProfessor("22222");
        professor.setShift("Nocturno");

        assertThrows(DataAccessException.class, () -> {
            professorDAO.saveProfessor(professor);
        });
    }

    @Test
    public void existsStaffNumber_NormalFlow_ReturnsTrue() throws DataAccessException {
        String staffNumber = "54321";
        ProfessorDTO professor = createBaseProfessor(staffNumber);
        professorDAO.saveProfessor(professor);

        boolean result = professorDAO.existsStaffNumber(staffNumber);
        assertTrue(result);
    }

    @Test
    public void existsStaffNumber_AlternateFlow_NotExists_ReturnsFalse() throws DataAccessException {
        boolean result = professorDAO.existsStaffNumber("00000");
        assertFalse(result);
    }

    @Test
    public void getActiveProfessorsCount_NormalFlow_ReturnsCount() throws DataAccessException {
        int initialCount = professorDAO.getActiveProfessorsCount();

        ProfessorDTO professor = createBaseProfessor("77777");
        professorDAO.saveProfessor(professor);

        int newCount = professorDAO.getActiveProfessorsCount();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    public void getActiveProfessorsCount_AlternateFlow_NoActiveProfessors() throws DataAccessException, SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM Profesor_Detalle");
             PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM Usuario WHERE rol = 'Profesor'")) {
            stmt.executeUpdate();
            stmt2.executeUpdate();
        }

        int count = professorDAO.getActiveProfessorsCount();
        assertEquals(0, count);
    }

    @Test
    public void inactivateProfessor_NormalFlow_ReturnsTrue() throws SQLException, DataAccessException {
        int savedId = insertDirectProfessorAndGetId("88888");

        boolean result = professorDAO.inactivateProfessor(savedId);
        assertTrue(result);
    }

    @Test
    public void inactivateProfessor_AlternateFlow_NonExistentId_ReturnsFalse() throws DataAccessException {
        boolean result = professorDAO.inactivateProfessor(999999);
        assertFalse(result);
    }

    @Test
    public void inactivateProfessor_AlternateFlow_AlreadyInactive() throws SQLException, DataAccessException {
        int savedId = insertDirectProfessorAndGetId("44444");
        professorDAO.inactivateProfessor(savedId);

        boolean resultSecondTime = professorDAO.inactivateProfessor(savedId);
        assertTrue(resultSecondTime);
    }

    private ProfessorDTO createBaseProfessor(String staffNumber) {
        ProfessorDTO professor = new ProfessorDTO();
        professor.setName("Test Professor");
        professor.setPassword("password123");
        professor.setState("Activo");
        professor.setStaffNumber(staffNumber);
        professor.setShift("Matutino");
        return professor;
    }

    private int insertDirectProfessorAndGetId(String staffNumber) throws SQLException {
        int generatedId = -1;
        String sqlUser = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES ('Direct Test', 'pass', 'Activo', 'Profesor')";
        try (PreparedStatement stmt = connection.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        }

        if (generatedId != -1) {
            String sqlProf = "INSERT INTO Profesor_Detalle (id_usuario, numero_personal, turno) VALUES (?, ?, 'Matutino')";
            try (PreparedStatement stmt2 = connection.prepareStatement(sqlProf)) {
                stmt2.setInt(1, generatedId);
                stmt2.setString(2, staffNumber);
                stmt2.executeUpdate();
            }
        }
        return generatedId;
    }
}