package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IProfessorDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import mx.uv.spp.utils.LogConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class ProfessorDAOImplementation implements IProfessorDAO {

    private final Connection connection;
    private static final Logger LOG = LogConfig.getLogger(ProfessorDAOImplementation.class);

    private static final String SQL_EXISTS_STAFF_NUMBER = "SELECT COUNT(*) FROM Profesor_Detalle WHERE numero_personal = ?";
    private static final String SQL_COUNT_ACTIVE = "SELECT COUNT(*) FROM Usuario WHERE rol = 'Profesor' AND estado = 'Activo'";
    private static final String SQL_SAVE_USER = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES (?, ?, ?, 'Profesor')";
    private static final String SQL_SAVE_PROFESSOR = "INSERT INTO Profesor_Detalle (id_usuario, numero_personal, turno) VALUES (?, ?, ?)";
    private static final String SQL_INACTIVATE_PROFESSOR_BY_ID = "UPDATE Usuario SET estado = 'No Activo' WHERE id_usuario = ?";

    public ProfessorDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean existsStaffNumber(String staffNumber) throws DataAccessException {
        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_STAFF_NUMBER)) {
            statement.setString(1, staffNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exists = resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error al verificar el numero de personal " + staffNumber + ": " + e.getMessage());
            throw new DataAccessException("Error checking staff number: " + staffNumber, e);
        }
        return exists;
    }

    @Override
    public int getActiveProfessorsCount() throws DataAccessException {
        int count = 0;
        try (PreparedStatement statement = connection.prepareStatement(SQL_COUNT_ACTIVE);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOG.severe("Error al contar los profesores activos: " + e.getMessage());
            throw new DataAccessException("Error counting active professors", e);
        }
        return count;
    }

    @Override
    public boolean saveProfessor(ProfessorDTO professor) throws DataAccessException {
        boolean isSaved = false;
        try {
            int generatedId = -1;
            try (PreparedStatement statementUser = connection.prepareStatement(SQL_SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
                statementUser.setString(1, professor.getName());
                statementUser.setString(2, professor.getPassword());
                statementUser.setString(3, professor.getState());
                statementUser.executeUpdate();
                try (ResultSet resultSet = statementUser.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = resultSet.getInt(1);
                    }
                }
            }
            if (generatedId != -1) {
                try (PreparedStatement statementProf = connection.prepareStatement(SQL_SAVE_PROFESSOR)) {
                    statementProf.setInt(1, generatedId);
                    statementProf.setString(2, professor.getStaffNumber());
                    statementProf.setString(3, professor.getShift());
                    isSaved = statementProf.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            LOG.severe("Error al guardar el profesor: " + e.getMessage());
            throw new DataAccessException("Error saving professor", e);
        }
        return isSaved;
    }

    @Override
    public boolean inactivateProfessor(int userId) throws DataAccessException {
        boolean isInactivated = false;
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE_PROFESSOR_BY_ID)) {
            statement.setInt(1, userId);
            isInactivated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.severe("Error al inactivar el profesor con ID " + userId + ": " + e.getMessage());
            throw new DataAccessException("Error inactivating professor with ID: " + userId, e);
        }
        return isInactivated;
    }
}