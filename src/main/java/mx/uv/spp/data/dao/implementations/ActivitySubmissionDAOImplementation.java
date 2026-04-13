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

public class ActivitySubmissionDAOImplementation implements IActivitySubmissionDAO {

    private final Connection connection;

    private static final String SQL_SAVE_ACTIVITY_SUBMISSION = "INSERT INTO Entrega_Actividad (nombre_archivo, fecha_entrega, calificacion, estado, id_actividad, id_practicante) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_ACTIVITY_SUBMISSION_BY_ID = "SELECT * FROM Entrega_Actividad WHERE id_entrega = ?";
    private static final String SQL_FIND_ALL_ACTIVITY_SUBMISSIONS = "SELECT * FROM Entrega_Actividad";
    private static final String SQL_UPDATE_ACTIVITY_SUBMISSION = "UPDATE Entrega_Actividad SET nombre_archivo=?, fecha_entrega=?, calificacion=?, estado=?, id_actividad=?, id_practicante=? WHERE id_entrega=?";
    private static final String SQL_DELETE_ACTIVITY_SUBMISSION_BY_ID = "DELETE FROM Entrega_Actividad WHERE id_entrega = ?";
    public ActivitySubmissionDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveActivitySubmission(ActivitySubmissionDTO submission) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_ACTIVITY_SUBMISSION)) {
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
    public ActivitySubmissionDTO findActivitySubmissionById(int id) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ACTIVITY_SUBMISSION_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return mapResultSetToSubmission(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar la entrega con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<ActivitySubmissionDTO> findAllActivitySubmissions() throws DatabaseException {
        List<ActivitySubmissionDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ACTIVITY_SUBMISSIONS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) list.add(mapResultSetToSubmission(resultSet));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar las entregas de actividades", e);
        }
        return list;
    }

    @Override
    public boolean updateActivitySubmission(ActivitySubmissionDTO submission) throws DatabaseException {
        try (PreparedStatement st = connection.prepareStatement(SQL_UPDATE_ACTIVITY_SUBMISSION)) {
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
    public boolean deleteActivitySubmissionById(Integer id) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ACTIVITY_SUBMISSION_BY_ID)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
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