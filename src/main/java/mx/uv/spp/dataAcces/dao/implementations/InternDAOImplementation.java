package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IInternDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternDAOImplementation implements IInternDAO {

    private final Connection connection;

    private static final String SQL_EXISTS_MATRICULA = "SELECT COUNT(*) FROM Practicante WHERE matricula = ?";
    private static final String SQL_SAVE_USER = "INSERT INTO Usuario (nombre, contrasena, estado, rol) VALUES (?, ?, ?, 'Practicante')";
    private static final String SQL_SAVE_INTERN = "INSERT INTO Practicante (id_usuario, matricula, genero, habla_lengua_indigena, lengua_indigena, id_coordinador, id_profesor) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INACTIVATE = "UPDATE Usuario SET estado = 'No Activo' WHERE id_usuario = ?";
    private static final String SQL_FIND_BY_ID = "SELECT u.id_usuario, u.nombre, u.contrasena, u.estado, p.matricula, p.genero, p.habla_lengua_indigena, p.lengua_indigena, p.id_coordinador, p.id_profesor FROM Usuario u INNER JOIN Practicante p ON u.id_usuario = p.id_usuario WHERE u.id_usuario = ?";
    private static final String SQL_FIND_ALL = "SELECT u.id_usuario, u.nombre, u.contrasena, u.estado, p.matricula, p.genero, p.habla_lengua_indigena, p.lengua_indigena, p.id_coordinador, p.id_profesor FROM Usuario u INNER JOIN Practicante p ON u.id_usuario = p.id_usuario WHERE u.rol = 'Practicante'";

    public InternDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean existsEnrollmentNumber(String enrollmentNumber) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_EXISTS_MATRICULA)) {
            statement.setString(1, enrollmentNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()){
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking enrollment number", e);
        }
        return false;
    }

    @Override
    public boolean saveIntern(InternDTO intern) throws DataAccessException {
        try {
            int generatedId = -1;
            try (PreparedStatement statementUser = connection.prepareStatement(SQL_SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
                statementUser.setString(1, intern.getName());
                statementUser.setString(2, intern.getPassword());
                statementUser.setString(3, intern.getState());
                statementUser.executeUpdate();
                try (ResultSet resultSet = statementUser.getGeneratedKeys()) {
                    if (resultSet.next()) generatedId = resultSet.getInt(1);
                }
            }

            if (generatedId != -1) {
                try (PreparedStatement statementIntern = connection.prepareStatement(SQL_SAVE_INTERN)) {
                    statementIntern.setInt(1, generatedId);
                    statementIntern.setString(2, intern.getEnrollmentNumber());
                    statementIntern.setString(3, intern.getGender());
                    statementIntern.setBoolean(4, intern.getSpeaksIndigenousLanguage());
                    statementIntern.setString(5, intern.getIndigenousLanguage());
                    statementIntern.setObject(6, intern.getCoordinatorId(), Types.INTEGER);
                    statementIntern.setObject(7, intern.getProfessorId(), Types.INTEGER);
                    return statementIntern.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving intern", e);
        }
    }

    @Override
    public boolean inactivateIntern(int userId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INACTIVATE)) {
            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error inactivating intern ID: " + userId, e);
        }
    }

    @Override
    public InternDTO getInternById(int userId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToIntern(resultSet);
                }
                InternDTO internDTO = new InternDTO();
                internDTO.setIdUser(-1);
                return internDTO;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding intern", e);
        }
    }

    @Override
    public List<InternDTO> getAllInterns() throws DataAccessException {
        List<InternDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) list.add(mapResultSetToIntern(resultSet));
        } catch (SQLException e) {
            throw new DataAccessException("Error listing interns", e);
        }
        return list;
    }

    private InternDTO mapResultSetToIntern(ResultSet resultSet) throws SQLException {
        InternDTO intern = new InternDTO();
        intern.setIdUser(resultSet.getInt("id_usuario"));
        intern.setUserId(resultSet.getInt("id_usuario"));
        intern.setName(resultSet.getString("nombre"));
        intern.setPassword(resultSet.getString("contrasena"));
        intern.setState(resultSet.getString("estado"));
        intern.setRole("Practicante");
        intern.setEnrollmentNumber(resultSet.getString("matricula"));
        intern.setGender(resultSet.getString("genero"));
        intern.setSpeaksIndigenousLanguage(resultSet.getBoolean("habla_lengua_indigena"));
        intern.setIndigenousLanguage(resultSet.getString("lengua_indigena"));
        intern.setCoordinatorId(resultSet.getObject("id_coordinador") != null ? resultSet.getInt("id_coordinador") : null);
        intern.setProfessorId(resultSet.getObject("id_profesor") != null ? resultSet.getInt("id_profesor") : null);
        return intern;
    }
}