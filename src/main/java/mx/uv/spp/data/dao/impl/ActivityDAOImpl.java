package mx.uv.spp.data.dao.impl;

import mx.uv.spp.business.dto.ActivityDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IActivityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAOImpl implements IActivityDAO {

    private final Connection connection;

    public ActivityDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveActivity(ActivityDTO activity) {
        String sql = "INSERT INTO activities (title, description, due_date, publication_date, status, professor_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, activity.getTitle());
            stmt.setString(2, activity.getDescription());
            stmt.setDate(3, new java.sql.Date(activity.getDueDate().getTime()));
            stmt.setDate(4, new java.sql.Date(activity.getPublicationDate().getTime()));
            stmt.setString(5, activity.getStatus());
            stmt.setInt(6, activity.getProfessorId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ActivityDTO findActivityById(Integer id) {
        String sql = "SELECT * FROM activities WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToActivity(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ActivityDTO> findAllActivities() {
        List<ActivityDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM activities";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean updateActivity(ActivityDTO activity) {
        String sql = "UPDATE activities SET title=?, description=?, due_date=?, publication_date=?, status=?, professor_id=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, activity.getTitle());
            stmt.setString(2, activity.getDescription());
            stmt.setDate(3, new java.sql.Date(activity.getDueDate().getTime()));
            stmt.setDate(4, new java.sql.Date(activity.getPublicationDate().getTime()));
            stmt.setString(5, activity.getStatus());
            stmt.setInt(6, activity.getProfessorId());
            stmt.setInt(7, activity.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
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