package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.AssignmentDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IAssignmentDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAOImplementation implements IAssignmentDAO {

    private final Connection connection;

    private static final String SQL_SAVE = "INSERT INTO Asignacion (fecha_asignacion, estado, id_practicante, id_proyecto, id_coordinador) VALUES (?, 'Activa', ?, ?, ?)";
    private static final String SQL_CHECK_EXISTING = "SELECT COUNT(*) FROM Asignacion WHERE id_practicante = ? AND estado = 'Activa'";
    private static final String SQL_INACTIVATE = "UPDATE Asignacion SET estado = 'Inactiva' WHERE id_asignacion = ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Asignacion WHERE id_asignacion = ?";
    private static final String SQL_FIND_BY_INTERN = "SELECT * FROM Asignacion WHERE id_practicante = ? AND estado = 'Activa'";
    private static final String SQL_FIND_BY_PROJECT = "SELECT * FROM Asignacion WHERE id_proyecto = ? AND estado = 'Activa'";

    public AssignmentDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveAssignment(AssignmentDTO assignment) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {
            statement.setDate(1, new java.sql.Date(assignment.getAssignmentDate().getTime()));
            statement.setInt(2, assignment.getInternId());
            statement.setInt(3, assignment.getProjectId());
            statement.setInt(4, assignment.getCoordinatorId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving the assignment", e);
        }
    }

    @Override
    public boolean hasExistingAssignment(int internId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_CHECK_EXISTING)) {
            statement.setInt(1, internId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking assignments for intern ID: " + internId, e);
        }
        return false;
    }

    @Override
    public boolean inactivateAssignment(int assignmentId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE)) {
            statement.setInt(1, assignmentId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error inactivating assignment ID: " + assignmentId, e);
        }
    }

    @Override
    public AssignmentDTO getAssignmentById(int assignmentId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, assignmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAssignment(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding assignment ID: " + assignmentId, e);
        }
        return new AssignmentDTO(-1);
    }

    @Override
    public AssignmentDTO getAssignmentByInternId(int internId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_INTERN)) {
            statement.setInt(1, internId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAssignment(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding assignment for intern ID: " + internId, e);
        }
        return new AssignmentDTO(-1);
    }

    @Override
    public List<AssignmentDTO> getAssignmentsByProjectId(int projectId) throws DataAccessException {
        List<AssignmentDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_PROJECT)) {
            statement.setInt(1, projectId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapResultSetToAssignment(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding assignments for project ID: " + projectId, e);
        }
        return list;
    }

    private AssignmentDTO mapResultSetToAssignment(ResultSet resultSet) throws SQLException {
        AssignmentDTO assignment = new AssignmentDTO();
        assignment.setId(resultSet.getInt("id_asignacion"));
        assignment.setAssignmentDate(resultSet.getDate("fecha_asignacion"));
        assignment.setStatus(resultSet.getString("estado"));
        assignment.setInternId(resultSet.getInt("id_practicante"));
        assignment.setProjectId(resultSet.getInt("id_proyecto"));
        assignment.setCoordinatorId(resultSet.getInt("id_coordinador"));
        return assignment;
    }
}