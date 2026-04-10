package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.ActivitySubmissionDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IActivitySubmissionDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivitySubmissionDAOImpl implements IActivitySubmissionDAO {

    private final Connection connection;

    public ActivitySubmissionDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveActivitySubmission(ActivitySubmissionDTO submission) {
        String sql = "INSERT INTO Entrega_Actividad (nombre_archivo, fecha_entrega, calificacion, estado, id_actividad, id_practicante) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, submission.getFileName());
            statement.setTimestamp(2, new java.sql.Timestamp(submission.getSubmissionDate().getTime()));
            statement.setObject(3, submission.getGrade());
            statement.setString(4, submission.getStatus());
            statement.setInt(5, submission.getActivityId());
            statement.setInt(6, submission.getInternId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar la entrega de actividad", e);
        }
    }

    @Override
    public ActivitySubmissionDTO findActivitySubmissionById(Integer id) {
        String sql = "SELECT * FROM Entrega_Actividad WHERE id_entrega = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapResultSetToSubmission(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar la entrega con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<ActivitySubmissionDTO> findAllActivitySubmissions() {
        List<ActivitySubmissionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Entrega_Actividad";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToSubmission(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar las entregas de actividades", e);
        }
        return list;
    }

    @Override
    public boolean updateActivitySubmission(ActivitySubmissionDTO submission) {
        String sql = "UPDATE Entrega_Actividad SET nombre_archivo=?, fecha_entrega=?, calificacion=?, estado=?, id_actividad=?, id_practicante=? WHERE id_entrega=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, submission.getFileName());
            st.setTimestamp(2, new java.sql.Timestamp(submission.getSubmissionDate().getTime()));
            st.setObject(3, submission.getGrade());
            st.setString(4, submission.getStatus());
            st.setInt(5, submission.getActivityId());
            st.setInt(6, submission.getInternId());
            st.setInt(7, submission.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar la entrega con ID: " + submission.getId(), e);
        }
    }

    @Override
    public boolean deleteActivitySubmissionById(Integer id) {
        String sql = "DELETE FROM Entrega_Actividad WHERE id_entrega = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar la entrega con ID: " + id, e);
        }
    }

    private ActivitySubmissionDTO mapResultSetToSubmission(ResultSet rs) throws SQLException {
        ActivitySubmissionDTO submission = new ActivitySubmissionDTO();
        submission.setId(rs.getInt("id_entrega"));
        submission.setFileName(rs.getString("nombre_archivo"));
        submission.setSubmissionDate(rs.getTimestamp("fecha_entrega"));
        submission.setGrade(rs.getObject("calificacion") != null ? rs.getDouble("calificacion") : null);
        submission.setStatus(rs.getString("estado"));
        submission.setActivityId(rs.getInt("id_actividad"));
        submission.setInternId(rs.getInt("id_practicante"));
        return submission;
    }
}