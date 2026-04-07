package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.ActivityDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IActivityDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAOImpl implements IActivityDAO {

    private final Connection connection;

    private static final String SQL_SAVE_ACTIVITY = "INSERT INTO activities (title, description, due_date, publication_date, status, professor_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_ACTIVITY_BY_ID = "SELECT * FROM activities WHERE id = ?";
    private static final String SQL_FIND_ALL_ACTIVITIES = "SELECT * FROM activities";
    private static final String SQL_UPDATE_ACTIVITY = "UPDATE activities SET title=?, description=?, due_date=?, publication_date=?, status=?, professor_id=? WHERE id=?";

    public ActivityDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveActivity(ActivityDTO activity) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_ACTIVITY)) {
            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setDate(3, new java.sql.Date(activity.getDueDate().getTime()));
            statement.setDate(4, new java.sql.Date(activity.getPublicationDate().getTime()));
            statement.setString(5, activity.getStatus());
            statement.setInt(6, activity.getProfessorId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar la actividad", e);
        }
    }

    @Override
    public ActivityDTO findActivityById(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ACTIVITY_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToActivity(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar la actividad con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<ActivityDTO> findAllActivities() {
        List<ActivityDTO> list = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ACTIVITIES);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToActivity(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar las actividades", e);
        }
        return list;
    }

    @Override
    public boolean updateActivity(ActivityDTO activity) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ACTIVITY)) {
            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setDate(3, new java.sql.Date(activity.getDueDate().getTime()));
            statement.setDate(4, new java.sql.Date(activity.getPublicationDate().getTime()));
            statement.setString(5, activity.getStatus());
            statement.setInt(6, activity.getProfessorId());
            statement.setInt(7, activity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar la actividad con ID: " + activity.getId(), e);
        }
    }

    private ActivityDTO mapResultSetToActivity(ResultSet resultSet) throws SQLException {
        ActivityDTO activity = new ActivityDTO();
        activity.setId(resultSet.getInt("id"));
        activity.setTitle(resultSet.getString("title"));
        activity.setDescription(resultSet.getString("description"));
        activity.setDueDate(resultSet.getDate("due_date"));
        activity.setPublicationDate(resultSet.getDate("publication_date"));
        activity.setStatus(resultSet.getString("status"));
        activity.setProfessorId(resultSet.getInt("professor_id"));
        return activity;
    }
}