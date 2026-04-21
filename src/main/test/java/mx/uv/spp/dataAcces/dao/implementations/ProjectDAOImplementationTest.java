package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.ProjectDTO;
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
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProjectDAOImplementationTest {

    private static final Logger LOGGER = Logger.getLogger(ProjectDAOImplementationTest.class.getName());
    private ProjectDAOImplementation projectDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            projectDAO = new ProjectDAOImplementation();

            String insertCompany = "INSERT IGNORE INTO Empresa (id_empresa, nombre_empresa, sector, nombre_responsable) VALUES (1, 'Tech', 'IT', 'John Doe')";
            try (PreparedStatement st = connection.prepareStatement(insertCompany)) {
                st.executeUpdate();
            }

            String insertUser = "INSERT IGNORE INTO Usuario (id_usuario, nombre, contrasena, estado, rol) VALUES (1, 'Coord', 'pass', 'Activo', 'Coordinador')";
            try (PreparedStatement st = connection.prepareStatement(insertUser)) {
                st.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting up test database", e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            String[] cleanQueries = {
                    "DELETE FROM Proyecto",
                    "DELETE FROM Empresa WHERE id_empresa = 1",
                    "DELETE FROM Usuario WHERE id_usuario = 1"
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

    @Test
    public void saveProject_validData_returnsTrue() throws DataAccessException {
        ProjectDTO project = createBaseProject(1, 1);

        boolean result = projectDAO.saveProject(project);

        assertTrue(result);
    }

    @Test
    public void saveProject_invalidCompanyId_throwsDataAccessException() {
        ProjectDTO project = createBaseProject(999, 1);

        assertThrows(DataAccessException.class, () -> {
            projectDAO.saveProject(project);
        });
    }

    @Test
    public void saveProject_nullDescription_throwsDataAccessException() {
        ProjectDTO project = createBaseProject(1, 1);
        project.setDescription(null);

        assertThrows(DataAccessException.class, () -> {
            projectDAO.saveProject(project);
        });
    }

    @Test
    public void saveProject_zeroCapacity_throwsDataAccessException() {
        ProjectDTO project = createBaseProject(1, 1);
        project.setInternCapacity(0);

        assertThrows(DataAccessException.class, () -> {
            projectDAO.saveProject(project);
        });
    }

    @Test
    public void updateProject_existingProject_returnsTrue() throws DataAccessException {
        projectDAO.saveProject(createBaseProject(1, 1));
        ProjectDTO saved = projectDAO.getAllProjects().get(0);
        saved.setDescription("Updated description");

        boolean result = projectDAO.updateProject(saved);

        assertTrue(result);
    }

    @Test
    public void updateProject_nonExistentProject_returnsFalse() throws DataAccessException {
        ProjectDTO project = createBaseProject(1, 1);
        project.setId(999);

        boolean result = projectDAO.updateProject(project);

        assertFalse(result);
    }

    @Test
    public void updateProject_nullDescription_throwsDataAccessException() throws DataAccessException {
        projectDAO.saveProject(createBaseProject(1, 1));
        ProjectDTO saved = projectDAO.getAllProjects().get(0);
        saved.setDescription(null);

        assertThrows(DataAccessException.class, () -> {
            projectDAO.updateProject(saved);
        });
    }

    @Test
    public void deleteProject_existingProject_returnsTrue() throws DataAccessException {
        projectDAO.saveProject(createBaseProject(1, 1));
        int id = projectDAO.getAllProjects().get(0).getId();

        boolean result = projectDAO.deleteProject(id);

        assertTrue(result);
    }

    @Test
    public void deleteProject_nonExistentProject_returnsFalse() throws DataAccessException {
        boolean result = projectDAO.deleteProject(999);

        assertFalse(result);
    }

    @Test
    public void deleteProject_alreadyDeletedProject_returnsFalse() throws DataAccessException {
        projectDAO.saveProject(createBaseProject(1, 1));
        int id = projectDAO.getAllProjects().get(0).getId();
        projectDAO.deleteProject(id);

        boolean result = projectDAO.deleteProject(id);

        assertFalse(result);
    }

    @Test
    public void incrementAssignedInterns_availableProject_returnsTrue() throws DataAccessException {
        projectDAO.saveProject(createBaseProject(1, 1));
        int id = projectDAO.getAllProjects().get(0).getId();

        boolean result = projectDAO.incrementAssignedInterns(id);

        assertTrue(result);
        assertEquals(1, projectDAO.getProjectById(id).getAssignedInterns());
    }

    @Test
    public void incrementAssignedInterns_nonExistentProject_returnsFalse() throws DataAccessException {
        boolean result = projectDAO.incrementAssignedInterns(999);

        assertFalse(result);
    }

    @Test
    public void incrementAssignedInterns_reachesCapacity_updatesStatusToNoDisponible() throws DataAccessException {
        ProjectDTO project = createBaseProject(1, 1);
        project.setInternCapacity(1);
        projectDAO.saveProject(project);
        int id = projectDAO.getAllProjects().get(0).getId();

        projectDAO.incrementAssignedInterns(id);
        ProjectDTO updated = projectDAO.getProjectById(id);

        assertEquals("No Disponible", updated.getStatus());
    }

    @Test
    public void incrementAssignedInterns_atFullCapacity_returnsFalse() throws DataAccessException {
        ProjectDTO project = createBaseProject(1, 1);
        project.setInternCapacity(1);
        projectDAO.saveProject(project);
        int id = projectDAO.getAllProjects().get(0).getId();
        projectDAO.incrementAssignedInterns(id);

        boolean result = projectDAO.incrementAssignedInterns(id);

        assertFalse(result);
    }

    @Test
    public void getProjectById_existingId_returnsProject() throws DataAccessException {
        projectDAO.saveProject(createBaseProject(1, 1));
        int id = projectDAO.getAllProjects().get(0).getId();

        ProjectDTO result = projectDAO.getProjectById(id);

        assertNotNull(result);
    }

    @Test
    public void getProjectById_nonExistentId_returnsNull() throws DataAccessException {
        ProjectDTO result = projectDAO.getProjectById(999);

        assertNull(result);
    }

    @Test
    public void getAllProjects_withRecords_returnsList() throws DataAccessException {
        projectDAO.saveProject(createBaseProject(1, 1));

        List<ProjectDTO> result = projectDAO.getAllProjects();

        assertFalse(result.isEmpty());
    }

    @Test
    public void getAllProjects_noRecords_returnsEmptyList() throws DataAccessException {
        List<ProjectDTO> result = projectDAO.getAllProjects();

        assertTrue(result.isEmpty());
    }

    @Test
    public void getAvailableProjects_hasAvailableProjects_returnsList() throws DataAccessException {
        projectDAO.saveProject(createBaseProject(1, 1));

        List<ProjectDTO> result = projectDAO.getAvailableProjects();

        assertFalse(result.isEmpty());
    }

    @Test
    public void getAvailableProjects_noAvailableProjects_returnsEmptyList() throws DataAccessException {
        ProjectDTO project = createBaseProject(1, 1);
        project.setInternCapacity(1);
        projectDAO.saveProject(project);
        int id = projectDAO.getAllProjects().get(0).getId();
        projectDAO.incrementAssignedInterns(id);

        List<ProjectDTO> result = projectDAO.getAvailableProjects();

        assertTrue(result.isEmpty());
    }

    private ProjectDTO createBaseProject(int companyId, int coordinatorId) {
        ProjectDTO project = new ProjectDTO();
        project.setDescription("Test Description");
        project.setStartDate(new Date());
        project.setEndDate(new Date(System.currentTimeMillis() + 86400000L));
        project.setInternCapacity(2);
        project.setCompanyId(companyId);
        project.setCoordinatorId(coordinatorId);
        return project;
    }
}