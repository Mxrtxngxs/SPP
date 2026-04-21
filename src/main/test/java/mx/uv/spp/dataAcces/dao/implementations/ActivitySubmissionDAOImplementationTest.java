package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.ActivitySubmissionDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class ActivitySubmissionDAOImplementationTest {

    private static final Logger LOGGER = Logger.getLogger(
            ActivitySubmissionDAOImplementationTest.class.getName());
    private ActivitySubmissionDAOImplementation submissionDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            submissionDAO = new ActivitySubmissionDAOImplementation();

            String insertUsers = "INSERT IGNORE INTO Usuario " +
                    "(id_usuario, nombre, contrasena, estado, rol) VALUES " +
                    "(1, 'Prof', 'pass', 'Activo', 'Profesor'), " +
                    "(2, 'Prac', 'pass', 'Activo', 'Practicante')";
            try (PreparedStatement statement = connection.prepareStatement(insertUsers)) {
                statement.executeUpdate();
            }

            String insertActivity = "INSERT IGNORE INTO Actividad " +
                    "(id_actividad, titulo, descripcion, fecha_limite, " +
                    "fecha_publicacion, estado, id_profesor) VALUES " +
                    "(1, 'T', 'D', '2026-12-31', '2026-01-01', 'Activa', 1), " +
                    "(2, 'T2', 'D2', '2026-12-31', '2026-01-01', 'Activa', 1)";
            try (PreparedStatement statement = connection.prepareStatement(insertActivity)) {
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting up test database", e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            String[] cleanQueries = {
                    "DELETE FROM Entrega_Actividad",
                    "DELETE FROM Actividad",
                    "DELETE FROM Usuario"
            };
            for (String query : cleanQueries) {
                try (PreparedStatement st = connection.prepareStatement(query)) {
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up test database", e);
        }
    }

    // --- saveActivitySubmission Tests ---

    @Test
    public void saveActivitySubmission_validData_returnsTrue()
            throws DataAccessException {
        ActivitySubmissionDTO submission = createBaseSubmission(1, 2);

        boolean result = submissionDAO.saveActivitySubmission(submission);

        assertTrue(result);
    }

    @Test
    public void saveActivitySubmission_invalidInternId_throwsDataAccessException() {
        ActivitySubmissionDTO submission = createBaseSubmission(1, 999);

        assertThrows(DataAccessException.class, () -> {
            submissionDAO.saveActivitySubmission(submission);
        });
    }

    @Test
    public void saveActivitySubmission_nullGrade_returnsTrue()
            throws DataAccessException {
        ActivitySubmissionDTO submission = createBaseSubmission(1, 2);
        submission.setGrade(null);

        boolean result = submissionDAO.saveActivitySubmission(submission);

        assertTrue(result);
    }

    @Test
    public void saveActivitySubmission_duplicateSubmission_throwsException()
            throws DataAccessException {
        ActivitySubmissionDTO submission = createBaseSubmission(1, 2);
        submissionDAO.saveActivitySubmission(submission);

        assertThrows(DataAccessException.class, () -> {
            submissionDAO.saveActivitySubmission(submission);
        });
    }

    // --- findActivitySubmissionById Tests ---

    @Test
    public void findActivitySubmissionById_existingId_returnsDto()
            throws DataAccessException {
        ActivitySubmissionDTO submission = createBaseSubmission(1, 2);
        submissionDAO.saveActivitySubmission(submission);
        int id = submissionDAO.findAllActivitySubmissions().get(0).getId();

        ActivitySubmissionDTO result =
                submissionDAO.findActivitySubmissionById(id);

        assertNotNull(result);
        assertEquals("test_file.pdf", result.getFileName());
    }

    @Test
    public void findActivitySubmissionById_nonExistentId_returnsNull()
            throws DataAccessException {
        ActivitySubmissionDTO result =
                submissionDAO.findActivitySubmissionById(999);

        assertNull(result);
    }

    @Test
    public void findActivitySubmissionById_negativeId_returnsNull()
            throws DataAccessException {
        ActivitySubmissionDTO result =
                submissionDAO.findActivitySubmissionById(-1);

        assertNull(result);
    }

    // --- findAllActivitySubmissions Tests ---

    @Test
    public void findAllActivitySubmissions_existingRecords_returnsList()
            throws DataAccessException {
        submissionDAO.saveActivitySubmission(createBaseSubmission(1, 2));

        List<ActivitySubmissionDTO> result =
                submissionDAO.findAllActivitySubmissions();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void findAllActivitySubmissions_noRecords_returnsEmptyList()
            throws DataAccessException {
        List<ActivitySubmissionDTO> result =
                submissionDAO.findAllActivitySubmissions();

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllActivitySubmissions_multipleRecords_returnsList()
            throws DataAccessException {
        submissionDAO.saveActivitySubmission(createBaseSubmission(1, 2));
        submissionDAO.saveActivitySubmission(createBaseSubmission(2, 2));

        List<ActivitySubmissionDTO> result =
                submissionDAO.findAllActivitySubmissions();

        assertEquals(2, result.size());
    }

    // --- updateActivitySubmission Tests ---

    @Test
    public void updateActivitySubmission_existingRecord_returnsTrue()
            throws DataAccessException {
        submissionDAO.saveActivitySubmission(createBaseSubmission(1, 2));
        ActivitySubmissionDTO saved =
                submissionDAO.findAllActivitySubmissions().get(0);
        saved.setFileName("updated.pdf");
        saved.setGrade(9.5);

        boolean result = submissionDAO.updateActivitySubmission(saved);

        assertTrue(result);
    }

    @Test
    public void updateActivitySubmission_nonExistentRecord_returnsFalse()
            throws DataAccessException {
        ActivitySubmissionDTO submission = createBaseSubmission(1, 2);
        submission.setId(999);

        boolean result = submissionDAO.updateActivitySubmission(submission);

        assertFalse(result);
    }

    @Test
    public void updateActivitySubmission_invalidGrade_throwsException()
            throws DataAccessException {
        submissionDAO.saveActivitySubmission(createBaseSubmission(1, 2));
        ActivitySubmissionDTO saved =
                submissionDAO.findAllActivitySubmissions().get(0);
        saved.setGrade(15.0);

        assertThrows(DataAccessException.class, () -> {
            submissionDAO.updateActivitySubmission(saved);
        });
    }

    // --- deleteActivitySubmissionById Tests ---

    @Test
    public void deleteActivitySubmissionById_existingId_returnsTrue()
            throws DataAccessException {
        submissionDAO.saveActivitySubmission(createBaseSubmission(1, 2));
        int id = submissionDAO.findAllActivitySubmissions().get(0).getId();

        boolean result = submissionDAO.deleteActivitySubmissionById(id);

        assertTrue(result);
    }

    @Test
    public void deleteActivitySubmissionById_nonExistentId_returnsFalse()
            throws DataAccessException {
        boolean result = submissionDAO.deleteActivitySubmissionById(999);

        assertFalse(result);
    }

    @Test
    public void deleteActivitySubmissionById_deletedTwice_returnsFalse()
            throws DataAccessException {
        submissionDAO.saveActivitySubmission(createBaseSubmission(1, 2));
        int id = submissionDAO.findAllActivitySubmissions().get(0).getId();

        submissionDAO.deleteActivitySubmissionById(id);
        boolean result = submissionDAO.deleteActivitySubmissionById(id);

        assertFalse(result);
    }

    // --- Private Helper Methods ---

    private ActivitySubmissionDTO createBaseSubmission(int actId, int internId) {
        ActivitySubmissionDTO submission = new ActivitySubmissionDTO();
        submission.setFileName("test_file.pdf");
        submission.setSubmissionDate(new Date());
        submission.setGrade(10.0);
        submission.setStatus("Entregada");
        submission.setActivityId(actId);
        submission.setInternId(internId);
        return submission;
    }
}