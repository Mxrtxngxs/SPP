package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IUserDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements IUserDAO {

    private final Connection connection;

    private static final String SQL_SAVE_USER = "INSERT INTO users (name, password, state, role) VALUES (?, ?, ?, ?)";
    private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id_user = ?";
    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_UPDATE_USER = "UPDATE users SET name=?, password=?, state=?, role=? WHERE id_user=?";

    public UserDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveUser(UserDTO user) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_USER)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getState());
            statement.setString(4, user.getRole());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al guardar el usuario", e);
        }
    }

    @Override
    public UserDTO findUserById(String id) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_USER_BY_ID)) {
            statement.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new DataAccessException("Error al buscar el usuario con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<UserDTO> findAllUsers() throws DataAccessException {
        List<UserDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_USERS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar los usuarios", e);
        }
        return list;
    }

    @Override
    public boolean updateUser(UserDTO user) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USER)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getState());
            statement.setString(4, user.getRole());
            statement.setInt(5, user.getIdUser());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar el usuario con ID: " + user.getIdUser(), e);
        }
    }

    private UserDTO mapResultSetToUser(ResultSet resultSet) throws SQLException {
        UserDTO user = new UserDTO();
        user.setIdUser(resultSet.getInt("id_user"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setState(resultSet.getString("state"));
        user.setRole(resultSet.getString("role"));
        return user;
    }
}