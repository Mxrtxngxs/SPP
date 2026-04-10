package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.CoordinatorDetailDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.ICoordinatorDetailDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CoordinatorDAOImplementation implements ICoordinatorDetailDAO {

    private final Connection connection;

    private static final String SQL_EXISTS_STAFF_NUMBER = "SELECT COUNT(*) FROM Coordinador_Detalle WHERE numero_personal = ?";
    private static final String SQL_INACTIVATE_COORDINATORS = "UPDATE Usuario SET estado = 'No Activo' WHERE rol = 'Coordinador'";
    private static final String SQL_SAVE_USER = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES (?, ?, ?, 'Coordinador')";
    private static final String SQL_SAVE_COORDINATOR = "INSERT INTO Coordinador_Detalle (id_usuario, numero_personal) VALUES (?, ?)";

    public CoordinatorDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean existsStaffNumber(String staffNumber) {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_STAFF_NUMBER)) {
            statement.setString(1, staffNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking staff number: " + staffNumber, e);
        }
        return false;
    }

    @Override
    public boolean inactivateCurrentCoordinators() {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE_COORDINATORS)) {
            return statement.executeUpdate() >= 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error inactivating current coordinators", e);
        }
    }

    @Override
    public boolean saveCoordinator( CoordinatorDetailDTO coordinator) {
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
                try (PreparedStatement statementCoordinator = connection.prepareStatement(SQL_SAVE_COORDINATOR)) {
                    statementCoordinator.setInt(1, generatedId);
                    statementCoordinator.setString(2, coordinator.getStaffNumber());
                    return statementCoordinator.executeUpdate() > 0;
                }
            }
            return false;

        } catch (SQLException e) {
            throw new DatabaseException("Error saving coordinator", e);
        }
    }
}