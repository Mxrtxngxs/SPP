package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.ActivityDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IActivityDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import mx.uv.spp.utils.LogConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ActivityDAOImplementation implements IActivityDAO {

    private final Connection connection;
    private static final Logger LOG = LogConfig.getLogger(ActivityDAOImplementation.class);

    private static final String SQL_SAVE_ACTIVITY = "INSERT INTO Actividad (titulo, descripcion, fecha_limite, fecha_publicacion, estado, id_profesor) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_ACTIVITY = "UPDATE Actividad SET titulo=?, descripcion=?, fecha_limite=?, fecha_publicacion=?, estado=?, id_profesor=? WHERE id_actividad=?";
    private static final String SQL_FIND_ACTIVITY_BY_ID = "SELECT * FROM Actividad WHERE id_actividad = ?";
    private static final String SQL_FIND_BY_PROFESSOR = "SELECT * FROM Actividad WHERE id_profesor = ?";

    public ActivityDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveActivity(ActivityDTO activity) throws DataAccessException {
        boolean isSaved = false;
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_ACTIVITY)) {
            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setDate(3, new java.sql.Date(activity.getDueDate().getTime()));
            statement.setTimestamp(4, new java.sql.Timestamp(activity.getPublicationDate().getTime()));
            statement.setString(5, activity.getStatus());
            statement.setInt(6, activity.getProfessorId());
            isSaved = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.severe("Error al guardar la actividad: " + e.getMessage());
            throw new DataAccessException("Error saving activity", e);
        }
        return isSaved;
    }

    @Override
    public boolean updateActivity(ActivityDTO activity) throws DataAccessException {
        boolean isUpdated = false;
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ACTIVITY)) {
            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setDate(3, new java.sql.Date(activity.getDueDate().getTime()));
            statement.setTimestamp(4, new java.sql.Timestamp(activity.getPublicationDate().getTime()));
            statement.setString(5, activity.getStatus());
            statement.setInt(6, activity.getProfessorId());
            statement.setInt(7, activity.getId());
            isUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.severe("Error al actualizar la actividad con ID " + activity.getId() + ": " + e.getMessage());
            throw new DataAccessException("Error updating activity", e);
        }
        return isUpdated;
    }

    @Override
    public ActivityDTO getActivityById(int activityId) throws DataAccessException {
        ActivityDTO activity = new ActivityDTO(-1);
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ACTIVITY_BY_ID)) {
            statement.setInt(1, activityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    activity = mapResultSetToActivity(resultSet);
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error al buscar la actividad con ID " + activityId + ": " + e.getMessage());
            throw new DataAccessException("Error finding activity with ID: " + activityId, e);
        }
        return activity;
    }

    @Override
    public List<ActivityDTO> getActivitiesByProfessorId(int professorId) throws DataAccessException {
        return getActivityList(SQL_FIND_BY_PROFESSOR, professorId);
    }

    private List<ActivityDTO> getActivityList(String query, int id) throws DataAccessException {
        List<ActivityDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapResultSetToActivity(resultSet));
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error al listar actividades para ID " + id + ": " + e.getMessage());
            throw new DataAccessException("Error listing activities", e);
        }
        return list;
    }

    private ActivityDTO mapResultSetToActivity(ResultSet resultSet) throws SQLException {
        ActivityDTO activity = new ActivityDTO();
        activity.setId(resultSet.getInt("id_actividad"));
        activity.setTitle(resultSet.getString("titulo"));
        activity.setDescription(resultSet.getString("descripcion"));
        activity.setDueDate(resultSet.getDate("fecha_limite"));
        activity.setPublicationDate(resultSet.getTimestamp("fecha_publicacion"));
        activity.setStatus(resultSet.getString("estado"));
        activity.setProfessorId(resultSet.getInt("id_profesor"));
        return activity;
    }
}