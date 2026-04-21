package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.ProjectDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IProjectDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImplementation implements IProjectDAO {

    private final Connection connection;

    private static final String SQL_SAVE_PROJECT = "INSERT INTO Proyecto (descripcion, fecha_inicio, fecha_fin, capacidad_practicantes, id_empresa, id_coordinador) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_PROJECT = "UPDATE Proyecto SET descripcion=?, fecha_inicio=?, fecha_fin=?, capacidad_practicantes=?, id_empresa=? WHERE id_proyecto=?";
    private static final String SQL_DELETE_PROJECT = "DELETE FROM Proyecto WHERE id_proyecto=?";
    private static final String SQL_INCREMENT_ASSIGNED = "UPDATE Proyecto SET practicantes_asignados = practicantes_asignados + 1, estado = IF(practicantes_asignados >= capacidad_practicantes, 'No Disponible', 'Disponible') WHERE id_proyecto = ? AND practicantes_asignados < capacidad_practicantes";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Proyecto WHERE id_proyecto=?";
    private static final String SQL_FIND_ALL = "SELECT * FROM Proyecto";
    private static final String SQL_FIND_AVAILABLE = "SELECT * FROM Proyecto WHERE estado = 'Disponible' AND practicantes_asignados < capacidad_practicantes";

    public ProjectDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveProject(ProjectDTO project) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_PROJECT)) {
            statement.setString(1, project.getDescription());
            statement.setDate(2, new java.sql.Date(project.getStartDate().getTime()));
            statement.setDate(3, new java.sql.Date(project.getEndDate().getTime()));
            statement.setInt(4, project.getInternCapacity());
            statement.setInt(5, project.getCompanyId());
            statement.setInt(6, project.getCoordinatorId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving project", e);
        }
    }

    @Override
    public boolean updateProject(ProjectDTO project) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PROJECT)) {
            statement.setString(1, project.getDescription());
            statement.setDate(2, new java.sql.Date(project.getStartDate().getTime()));
            statement.setDate(3, new java.sql.Date(project.getEndDate().getTime()));
            statement.setInt(4, project.getInternCapacity());
            statement.setInt(5, project.getCompanyId());
            statement.setInt(6, project.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating project ID: " + project.getId(), e);
        }
    }

    @Override
    public boolean deleteProject(int projectId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_PROJECT)) {
            statement.setInt(1, projectId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting project ID: " + projectId, e);
        }
    }

    @Override
    public boolean incrementAssignedInterns(int projectId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INCREMENT_ASSIGNED)) {
            statement.setInt(1, projectId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error incrementing assigned interns for project ID: " + projectId, e);
        }
    }

    @Override
    public ProjectDTO getProjectById(int projectId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, projectId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProject(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding project ID: " + projectId, e);
        }
        return null;
    }

    @Override
    public List<ProjectDTO> getAllProjects() throws DataAccessException {
        return getProjectList(SQL_FIND_ALL);
    }

    @Override
    public List<ProjectDTO> getAvailableProjects() throws DataAccessException {
        return getProjectList(SQL_FIND_AVAILABLE);
    }

    private List<ProjectDTO> getProjectList(String query) throws DataAccessException {
        List<ProjectDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToProject(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing projects", e);
        }
        return list;
    }

    private ProjectDTO mapResultSetToProject(ResultSet rs) throws SQLException {
        ProjectDTO project = new ProjectDTO();
        project.setId(rs.getInt("id_proyecto"));
        project.setDescription(rs.getString("descripcion"));
        project.setStartDate(rs.getDate("fecha_inicio"));
        project.setEndDate(rs.getDate("fecha_fin"));
        project.setInternCapacity(rs.getInt("capacidad_practicantes"));
        project.setAssignedInterns(rs.getInt("practicantes_asignados"));
        project.setStatus(rs.getString("estado"));
        project.setCompanyId(rs.getInt("id_empresa"));
        project.setCoordinatorId(rs.getInt("id_coordinador"));
        return project;
    }
}