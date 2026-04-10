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

    private static final String SQL_SAVE_ACTIVITY = "INSERT INTO activities (title, description, due_date, publication_date, status, professor_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_ACTIVITY_BY_ID = "SELECT * FROM activities WHERE id = ?";
    private static final String SQL_FIND_ALL_ACTIVITIES = "SELECT * FROM activities";
    private static final String SQL_UPDATE_ACTIVITY = "UPDATE activities SET title=?, description=?, due_date=?, publication_date=?, status=?, professor_id=? WHERE id=?";

    public ActivityDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }


    @Override
    public boolean saveActivity(ActivityDTO activity) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_ACTIVITY)){
            statement.setString(1, activity.getTitle());
        } catch (SQLException e){
            throw new DatabaseException("", e);
        }
        return false;
    }

    @Override
    public boolean updateActivity(ActivityDTO activity) {
        return false;
    }

    @Override
    public boolean deleteActivity(int activityId) {
        return false;
    }

    @Override
    public ActivityDTO getActivityById(int activityId) {
        return null;
    }

    @Override
    public List<ActivityDTO> getActivitiesByProfessorId(int professorId) {
        return List.of();
    }

    @Override
    public List<ActivityDTO> getActivitiesByProjectId(int projectId) {
        return List.of();
    }
}