package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.ActivitySubmissionDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IActivitySubmissionDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import mx.uv.spp.utils.LogConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ActivitySubmissionDAOImplementation implements IActivitySubmissionDAO {

    private final Connection connection;
    private static final Logger LOG = LogConfig.getLogger(ActivitySubmissionDAOImplementation.class);

    private static final String SQL_SAVE_ACTIVITY_SUBMISSION = "INSERT INTO Entrega_Actividad (nombre_archivo, fecha_entrega, calificacion, estado, id_actividad, id_practicante) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_ACTIVITY_SUBMISSION_BY_ID = "SELECT * FROM Entrega_Actividad WHERE id_entrega = ?";
    private static final String SQL_FIND_ALL_ACTIVITY_SUBMISSIONS = "SELECT * FROM Entrega_Actividad";
    private static final String SQL_UPDATE_ACTIVITY_SUBMISSION = "UPDATE Entrega_Actividad SET nombre_archivo=?, fecha_entrega=?, calificacion=?, estado=?, id_actividad=?, id_practicante=? WHERE id_entrega=?";
    private static final String SQL_DELETE_ACTIVITY_SUBMISSION_BY_ID = "DELETE FROM Entrega_Actividad WHERE id_entrega = ?";

    public ActivitySubmissionDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveActivitySubmission(ActivitySubmissionDTO submission) throws DataAccessException {
        boolean isSaved = false;
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_ACTIVITY_SUBMISSION)) {
            statement.setString(1, submission.getFileName());
            statement.setTimestamp(2, new java.sql.Timestamp(submission.getSubmissionDate().getTime()));
            statement.setObject(3, submission.getGrade());
            statement.setString(4, submission.getStatus());
            statement.setInt(5, submission.getActivityId());
            statement.setInt(6, submission.getInternId());
            isSaved = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.severe("Error al guardar la entrega de actividad: " + e.getMessage());
            throw new DataAccessException("Error al guardar la entrega de actividad", e);
        }
        return isSaved;
    }

    @Override
    public ActivitySubmissionDTO findActivitySubmissionById(int id) throws DataAccessException {
        ActivitySubmissionDTO submission = new ActivitySubmissionDTO(-1);
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ACTIVITY_SUBMISSION_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    submission = mapResultSetToSubmission(resultSet);
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error al buscar la entrega con ID " + id + ": " + e.getMessage());
            throw new DataAccessException("Error al buscar la entrega con ID: " + id, e);
        }
        return submission;
    }

    @Override
    public List<ActivitySubmissionDTO> findAllActivitySubmissions() throws DataAccessException {
        List<ActivitySubmissionDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ACTIVITY_SUBMISSIONS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToSubmission(resultSet));
            }
        } catch (SQLException e) {
            LOG.severe("Error al listar las entregas de actividades: " + e.getMessage());
            throw new DataAccessException("Error al listar las entregas de actividades", e);
        }
        return list;
    }

    @Override
    public boolean updateActivitySubmission(ActivitySubmissionDTO submission) throws DataAccessException {
        boolean isUpdated = false;
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ACTIVITY_SUBMISSION)) {
            statement.setString(1, submission.getFileName());
            statement.setTimestamp(2, new java.sql.Timestamp(submission.getSubmissionDate().getTime()));
            statement.setObject(3, submission.getGrade());
            statement.setString(4, submission.getStatus());
            statement.setInt(5, submission.getActivityId());
            statement.setInt(6, submission.getInternId());
            statement.setInt(7, submission.getId());
            isUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.severe("Error al actualizar la entrega con ID " + submission.getId() + ": " + e.getMessage());
            throw new DataAccessException("Error al actualizar la entrega con ID: " + submission.getId(), e);
        }
        return isUpdated;
    }

    @Override
    public boolean deleteActivitySubmissionById(Integer id) throws DataAccessException {
        boolean isDeleted = false;
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ACTIVITY_SUBMISSION_BY_ID)) {
            statement.setInt(1, id);
            isDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.severe("Error al eliminar la entrega con ID " + id + ": " + e.getMessage());
            throw new DataAccessException("Error al eliminar la entrega con ID: " + id, e);
        }
        return isDeleted;
    }

    private ActivitySubmissionDTO mapResultSetToSubmission(ResultSet resultSet) throws SQLException {
        ActivitySubmissionDTO submission = new ActivitySubmissionDTO();
        submission.setId(resultSet.getInt("id_entrega"));
        submission.setFileName(resultSet.getString("nombre_archivo"));
        submission.setSubmissionDate(resultSet.getTimestamp("fecha_entrega"));
        submission.setGrade(resultSet.getObject("calificacion") != null ? resultSet.getDouble("calificacion") : null);
        submission.setStatus(resultSet.getString("estado"));
        submission.setActivityId(resultSet.getInt("id_actividad"));
        submission.setInternId(resultSet.getInt("id_practicante"));
        return submission;
    }
}