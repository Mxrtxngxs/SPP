package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.ActivityDTO;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActivityDAOImplementationTest {

    private static final Logger LOGGER = Logger.getLogger(ActivityDAOImplementationTest.class.getName());
    private ActivityDAOImplementation activityDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            activityDAO = new ActivityDAOImplementation();

            String insertUser = "INSERT IGNORE INTO Usuario (id_usuario, nombre, contrasena, estado, rol) VALUES (1, 'Test User', 'pass123', 'Activo', 'Profesor')";
            try (PreparedStatement statement = connection.prepareStatement(insertUser)) {
                statement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting up test database", e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            String cleanActivities = "DELETE FROM Actividad";
            try (PreparedStatement statement = connection.prepareStatement(cleanActivities)) {
                statement.executeUpdate();
            }

            String cleanUsers = "DELETE FROM Usuario WHERE id_usuario = 1";
            try (PreparedStatement statement = connection.prepareStatement(cleanUsers)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up test database", e);
        }
    }

    @Test
    public void TestSaveActivitySuccess_validActivity_returnsTrue() throws DataAccessException {
        ActivityDTO activity = createBaseActivity(1);

        boolean result = activityDAO.saveActivity(activity);

        assertTrue(
                result);
    }

    @Test
    public void TestSaveActivityFail_nullDates_throwsNullPointerException() {
        ActivityDTO activity = new ActivityDTO();
        activity.setTitle("Test Title");
        activity.setProfessorId(1);

        assertThrows(NullPointerException.class, () -> {
            activityDAO.saveActivity(activity);
        });
    }

    @Test
    public void saveActivity_nonExistentProfessor_throwsDataAccessException() {
        ActivityDTO activity = createBaseActivity(999);

        assertThrows(DataAccessException.class, () -> {
            activityDAO.saveActivity(activity);
        });
    }

    @Test
    public void saveActivity_missingDescription_throwsDataAccessException() {
        ActivityDTO activity = createBaseActivity(1);
        activity.setDescription(null);

        assertThrows(DataAccessException.class, () -> {
            activityDAO.saveActivity(activity);
        });
    }

    @Test
    public void updateActivity_existingActivity_returnsTrue() throws DataAccessException {
        ActivityDTO activity = createBaseActivity(1);
        activityDAO.saveActivity(activity);

        List<ActivityDTO> activities = activityDAO.getActivitiesByProfessorId(1);
        ActivityDTO savedActivity = activities.get(0);
        savedActivity.setTitle("Updated Title");

        boolean result = activityDAO.updateActivity(savedActivity);

        assertTrue(result);
    }

    @Test
    public void updateActivity_nonExistentActivity_returnsFalse() throws DataAccessException {
        ActivityDTO activity = createBaseActivity(1);
        activity.setId(9999);

        boolean result = activityDAO.updateActivity(activity);

        assertFalse(result);
    }

    @Test
    public void updateActivity_invalidStatusEnum_throwsDataAccessException() throws DataAccessException {
        ActivityDTO activity = createBaseActivity(1);
        activityDAO.saveActivity(activity);

        List<ActivityDTO> activities = activityDAO.getActivitiesByProfessorId(1);
        ActivityDTO savedActivity = activities.get(0);
        savedActivity.setStatus("InvalidStatus");

        assertThrows(DataAccessException.class, () -> {
            activityDAO.updateActivity(savedActivity);
        });
    }

    @Test
    public void getActivityById_existingId_returnsActivity() throws DataAccessException {
        ActivityDTO activity = createBaseActivity(1);
        activityDAO.saveActivity(activity);

        int savedId = activityDAO.getActivitiesByProfessorId(1).get(0).getId();

        ActivityDTO result = activityDAO.getActivityById(savedId);

        assertNotNull(result);
        assertEquals("Activa", result.getStatus());
    }

    @Test
    public void getActivityById_nonExistentId_throwsDataAccessException() {
        int nonExistentId = 9;

        assertThrows(DataAccessException.class, () -> {
            activityDAO.getActivityById(nonExistentId);
        });
    }

    @Test
    public void getActivityById_negativeId_throwsDataAccessException() {
        assertThrows(DataAccessException.class, () -> {
            activityDAO.getActivityById(-1);
        });
    }

    @Test
    public void getActivitiesByProfessorId_existingProfessor_returnsList() throws DataAccessException {
        ActivityDTO activity = createBaseActivity(1);
        activityDAO.saveActivity(activity);

        List<ActivityDTO> result = activityDAO.getActivitiesByProfessorId(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void getActivitiesByProfessorId_professorWithNoActivities_returnsEmptyList() throws DataAccessException {
        List<ActivityDTO> result = activityDAO.getActivitiesByProfessorId(1);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getActivitiesByProfessorId_nonExistentProfessor_returnsEmptyList() throws DataAccessException {
        List<ActivityDTO> result = activityDAO.getActivitiesByProfessorId(999);

        assertTrue(result.isEmpty());
    }

    private ActivityDTO createBaseActivity(int professorId) {
        ActivityDTO activity = new ActivityDTO();
        activity.setTitle("Test Title");
        activity.setDescription("Test Description");
        activity.setDueDate(new Date());
        activity.setPublicationDate(new Date());
        activity.setStatus("Activa");
        activity.setProfessorId(professorId);
        return activity;
    }
}