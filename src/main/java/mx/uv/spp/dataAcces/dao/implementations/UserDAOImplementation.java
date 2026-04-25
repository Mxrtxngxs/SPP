package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IUserDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAOImplementation implements IUserDAO {

    private final Connection connection;

    private static final String SQL_SAVE_USER_COORDINATOR = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES (?, ?, 'Activo', 'Coordinador')";
    private static final String SQL_SAVE_COORDINATOR_DETAIL = "INSERT INTO Coordinador_Detalle (id_usuario, numero_personal) VALUES (?, ?)";

    private static final String SQL_SAVE_USER_PROFESSOR = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES (?, ?, 'Activo', 'Profesor')";
    private static final String SQL_SAVE_PROFESSOR_DETAIL = "INSERT INTO Profesor_Detalle (id_usuario, numero_personal, turno) VALUES (?, ?, ?)";

    private static final String SQL_SAVE_USER_INTERN = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES (?, ?, 'Activo', 'Practicante')";
    private static final String SQL_SAVE_INTERN_DETAIL = "INSERT INTO Practicante_Detalle (id_usuario, matricula, sexo, habla_lengua_indigena, lengua_indigena, id_coordinador, id_profesor) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INACTIVATE_USER = "UPDATE Usuario SET estado = 'No Activo' WHERE id_usuario = ?";

    private static final String SQL_GET_USER_BY_IDENTIFIER = "SELECT u.id_usuario, u.nombre, u.rol, u.estado, u.contrasena " +
            "FROM Usuario u " +
            "LEFT JOIN Coordinador_Detalle c ON u.id_usuario = c.id_usuario " +
            "LEFT JOIN Profesor_Detalle p ON u.id_usuario = p.id_usuario " +
            "LEFT JOIN Practicante_Detalle pr ON u.id_usuario = pr.id_usuario " +
            "WHERE (c.numero_personal = ? OR p.numero_personal = ? OR pr.matricula = ?)";

    public UserDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveCoordinator(CoordinatorDTO coordinator) throws DataAccessException {
        try {
            int generatedId = -1;
            try (PreparedStatement statementUser = connection.prepareStatement(SQL_SAVE_USER_COORDINATOR, Statement.RETURN_GENERATED_KEYS)) {
                statementUser.setString(1, coordinator.getName());
                statementUser.setString(2, coordinator.getPassword());
                statementUser.executeUpdate();
                try (ResultSet resultSet = statementUser.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = resultSet.getInt(1);
                    }
                }
            }

            if (generatedId != -1) {
                try (PreparedStatement statementDetail = connection.prepareStatement(SQL_SAVE_COORDINATOR_DETAIL)) {
                    statementDetail.setInt(1, generatedId);
                    statementDetail.setString(2, coordinator.getStaffNumber());
                    return statementDetail.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving coordinator", e);
        }
    }

    @Override
    public boolean saveProfessor(ProfessorDTO professor) throws DataAccessException {
        try {
            int generatedId = -1;
            try (PreparedStatement statementUser = connection.prepareStatement(SQL_SAVE_USER_PROFESSOR, Statement.RETURN_GENERATED_KEYS)) {
                statementUser.setString(1, professor.getName());
                statementUser.setString(2, professor.getPassword());
                statementUser.executeUpdate();
                try (ResultSet resultSet = statementUser.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = resultSet.getInt(1);
                    }
                }
            }

            if (generatedId != -1) {
                try (PreparedStatement statementDetail = connection.prepareStatement(SQL_SAVE_PROFESSOR_DETAIL)) {
                    statementDetail.setInt(1, generatedId);
                    statementDetail.setString(2, professor.getStaffNumber());
                    statementDetail.setString(3, professor.getShift());
                    return statementDetail.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving professor", e);
        }
    }

    @Override
    public boolean saveIntern(InternDTO intern) throws DataAccessException {
        try {
            int generatedId = -1;
            try (PreparedStatement statementUser = connection.prepareStatement(SQL_SAVE_USER_INTERN, Statement.RETURN_GENERATED_KEYS)) {
                statementUser.setString(1, intern.getName());
                statementUser.setString(2, intern.getPassword());
                statementUser.executeUpdate();
                try (ResultSet resultSet = statementUser.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedId = resultSet.getInt(1);
                    }
                }
            }

            if (generatedId != -1) {
                try (PreparedStatement statementDetail = connection.prepareStatement(SQL_SAVE_INTERN_DETAIL)) {
                    statementDetail.setInt(1, generatedId);
                    statementDetail.setString(2, intern.getEnrollmentNumber());
                    statementDetail.setString(3, intern.getGender());
                    statementDetail.setBoolean(4, intern.getSpeaksIndigenousLanguage());
                    statementDetail.setString(5, intern.getIndigenousLanguage());
                    statementDetail.setInt(6, intern.getCoordinatorId());
                    statementDetail.setInt(7, intern.getProfessorId());
                    return statementDetail.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving intern", e);
        }
    }

    @Override
    public boolean inactivateUser(int userId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE_USER)) {
            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error inactivating user ID: " + userId, e);
        }
    }

    @Override
    public UserDTO getUserByIdentifier(String identifier) throws DataAccessException {
        UserDTO user = new UserDTO();
        user.setIdUser(-1);

        try (PreparedStatement statement = connection.prepareStatement(SQL_GET_USER_BY_IDENTIFIER)) {
            statement.setString(1, identifier);
            statement.setString(2, identifier);
            statement.setString(3, identifier);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting user by identifier", e);
        }

        return user;
    }

    private UserDTO mapResultSetToUser(ResultSet resultSet) throws SQLException {
        UserDTO user = new UserDTO();
        user.setIdUser(resultSet.getInt("id_usuario"));
        user.setName(resultSet.getString("nombre"));
        user.setRole(resultSet.getString("rol"));
        user.setState(resultSet.getString("estado"));
        user.setPassword(resultSet.getString("contrasena"));
        return user;
    }
}