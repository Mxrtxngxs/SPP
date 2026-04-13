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

public class ActivityDAOImplementation implements IActivityDAO {

    private final Connection connection;

    private static final String SQL_SAVE_ACTIVITY = "INSERT INTO activities (title, description, due_date, publication_date, status, professor_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_ACTIVITY = " UPDATE activities SET title=?, description=?, due_date=?, publication_date=?, status=?, professor_id=?, project_id=? WHERE id=?";
    private static final String SQL_DELETE_ACTIVITY = "DELETE FROM activities WHERE id=?";
    private static final String SQL_FIND_ACTIVITY_BY_ID = "SELECT * FROM activities WHERE id = ?";
    private static final String SQL_FIND_BY_PROFESSOR = "SELECT * FROM activities WHERE professor_id = ?";
    private static final String SQL_FIND_BY_PROJECT = "SELECT * FROM activities WHERE project_id = ?";

    public ActivityDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveActivity(ActivityDTO activity) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_ACTIVITY)) {
            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setDate(3, new java.sql.Date(activity.getDueDate().getTime()));
            statement.setDate(4, new java.sql.Date(activity.getPublicationDate().getTime()));
            statement.setString(5, activity.getStatus());
            statement.setInt(6, activity.getProfessorId());
            statement.setInt(7, activity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving activity", e);
        }
    }

    @Override
    public boolean updateActivity(ActivityDTO activity) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ACTIVITY)) {
            statement.setString(1, activity.getTitle());
            statement.setString(2, activity.getDescription());
            statement.setDate(3, new java.sql.Date(activity.getDueDate().getTime()));
            statement.setDate(4, new java.sql.Date(activity.getPublicationDate().getTime()));
            statement.setString(5, activity.getStatus());
            statement.setInt(6, activity.getProfessorId());
            statement.setInt(7, activity.getId());
            statement.setInt(8, activity.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating activity", e);
        }
    }

    @Override
    public boolean deleteActivity(int activityId) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ACTIVITY)) {
            statement.setInt(1, activityId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting activity", e);
        }
    }

    @Override
    public ActivityDTO getActivityById(int activityId) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ACTIVITY_BY_ID)) {
            statement.setInt(1, activityId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return mapResultSetToActivity(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding activity", e);
        }
        return null;
    }

    @Override
    public List<ActivityDTO> getActivitiesByProfessorId(int professorId) throws DatabaseException {
        return getActivityList(SQL_FIND_BY_PROFESSOR, professorId);
    }

    @Override
    public List<ActivityDTO> getActivitiesByProjectId(int projectId) throws DatabaseException {
        return getActivityList(SQL_FIND_BY_PROJECT, projectId);
    }

    private List<ActivityDTO> getActivityList(String query, int id) throws DatabaseException {
        List<ActivityDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) list.add(mapResultSetToActivity(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing activities", e);
        }
        return list;
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
        activity.setId(resultSet.getInt("project_id"));
        return activity;
    }
}