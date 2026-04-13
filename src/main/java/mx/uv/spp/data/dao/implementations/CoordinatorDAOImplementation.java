package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.ICoordinatorDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

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

    public CoordinatorDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean existsStaffNumber(String staffNumber) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_STAFF_NUMBER)) {
            statement.setString(1, staffNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking staff number", e);
        }
        return false;
    }

    @Override
    public boolean inactivateCurrentCoordinators() throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE_COORDINATORS)) {
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error inactivating current coordinators", e);
        }
    }

    @Override
    public boolean saveCoordinator(CoordinatorDTO coordinator) throws DatabaseException {
        try {
            int generatedId = -1;
            try (PreparedStatement stmtUser = connection.prepareStatement(SQL_SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, coordinator.getName());
                stmtUser.setString(2, coordinator.getPassword());
                stmtUser.setString(3, coordinator.getState());
                stmtUser.executeUpdate();
                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }

            if (generatedId != -1) {
                try (PreparedStatement stmtCoord = connection.prepareStatement(SQL_SAVE_COORDINATOR)) {
                    stmtCoord.setInt(1, generatedId);
                    stmtCoord.setString(2, coordinator.getStaffNumber());
                    return stmtCoord.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving coordinator", e);
        }
    }

    @Override
    public boolean inactivateCoordinator(int userId) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE_COORDINATOR_BY_ID)) {
            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error inactivating coordinator ID: " + userId, e);
        }
    }

    @Override
    public CoordinatorDTO getCoordinatorById(int userId) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCoordinator(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding coordinator ID: " + userId, e);
        }
        return null;
    }

    @Override
    public List<CoordinatorDTO> getAllCoordinators() throws DatabaseException {
        List<CoordinatorDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToCoordinator(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing coordinators", e);
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