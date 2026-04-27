package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.ICoordinatorDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorDAOImplementation implements ICoordinatorDAO {

    private final Connection connection;

    private static final String SQL_EXISTS_STAFF_NUMBER = "SELECT COUNT(*) FROM Coordinador_Detalle WHERE numero_personal = ?";
    private static final String SQL_INACTIVATE_COORDINATORS = "UPDATE Usuario SET estado = 'No Activo' WHERE rol = 'Coordinador'";
    private static final String SQL_INACTIVATE_COORDINATOR_BY_ID = "UPDATE Usuario SET estado = 'No Activo' WHERE id_usuario = ?";
    private static final String SQL_SAVE_USER = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES (?, ?, ?, 'Coordinador')";
    private static final String SQL_SAVE_COORDINATOR = "INSERT INTO Coordinador_Detalle (id_usuario, numero_personal) VALUES (?, ?)";
    private static final String SQL_FIND_BY_ID = "SELECT u.id_usuario, u.nombre, u.contrasena, u.estado, c.numero_personal FROM Usuario u INNER JOIN Coordinador_Detalle c ON u.id_usuario = c.id_usuario WHERE u.id_usuario = ?";
    private static final String SQL_FIND_ALL = "SELECT u.id_usuario, u.nombre, u.contrasena, u.estado, c.numero_personal FROM Usuario u INNER JOIN Coordinador_Detalle c ON u.id_usuario = c.id_usuario WHERE u.rol = 'Coordinador'";
    private static final String SQL_FIND_ALL_ACTIVE_COORDINATORS = "SELECT u.id_usuario, u.nombre, u.contrasena, u.estado, c.numero_personal FROM Usuario u INNER JOIN Coordinador_Detalle c ON u.id_usuario = c.id_usuario WHERE u.rol = 'Coordinador' AND u.estado = 'Activo'";
    public CoordinatorDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean existsStaffNumber(String staffNumber) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_STAFF_NUMBER)) {
            statement.setString(1, staffNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking staff number", e);
        }
        return false;
    }

    @Override
    public boolean inactivateCurrentCoordinators() throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE_COORDINATORS)) {
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error inactivating current coordinators", e);
        }
    }

    @Override
    public boolean saveCoordinator(CoordinatorDTO coordinator) throws DataAccessException {
        try {
            int generatedId = -1;
            try (PreparedStatement statementUser = connection.prepareStatement(SQL_SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
                statementUser.setString(1, coordinator.getName());
                statementUser.setString(2, coordinator.getPassword());
                statementUser.setString(3, coordinator.getState());
                statementUser.executeUpdate();
                try (ResultSet rs = statementUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }

            if (generatedId != -1) {
                try (PreparedStatement statementCoord = connection.prepareStatement(SQL_SAVE_COORDINATOR)) {
                    statementCoord.setInt(1, generatedId);
                    statementCoord.setString(2, coordinator.getStaffNumber());
                    return statementCoord.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving coordinator", e);
        }
    }

    @Override
    public boolean inactivateCoordinator(int userId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE_COORDINATOR_BY_ID)) {
            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error inactivating coordinator ID: " + userId, e);
        }
    }

    @Override
    public CoordinatorDTO getCoordinatorById(int userId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCoordinator(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding coordinator ID: " + userId, e);
        }
        return new CoordinatorDTO(-1);
    }

    @Override
    public List<CoordinatorDTO> getAllCoordinators() throws DataAccessException {
        return List.of();
    }

    @Override
    public List<CoordinatorDTO> getActiveCoordinators() throws DataAccessException {
        List<CoordinatorDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToCoordinator(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing coordinators", e);
        }
        return list;
    }

    @Override
    public List<CoordinatorDTO> getAllActiveCoordinators() throws DataAccessException {
        List<CoordinatorDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_ACTIVE_COORDINATORS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToCoordinator(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing coordinators", e);
        }
        return list;
    }


    private CoordinatorDTO mapResultSetToCoordinator(ResultSet resultSet) throws SQLException {
        CoordinatorDTO coordinator = new CoordinatorDTO();
        coordinator.setIdUser(resultSet.getInt("id_usuario"));
        coordinator.setName(resultSet.getString("nombre"));
        coordinator.setPassword(resultSet.getString("contrasena"));
        coordinator.setState(resultSet.getString("estado"));
        coordinator.setStaffNumber(resultSet.getString("numero_personal"));
        coordinator.setRole("Coordinador");
        return coordinator;
    }
}