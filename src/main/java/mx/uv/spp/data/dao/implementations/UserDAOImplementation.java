package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IUserDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImplementation implements IUserDAO {

    private final Connection connection;

    public UserDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveUser(UserDTO user) {
        String sql = "INSERT INTO users (name, password, state, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getState());
            statement.setString(4, user.getRole());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar el usuario", e);
        }
    }

    @Override
    public UserDTO findUserById(String id) {
        String sql = "SELECT * FROM users WHERE id_user = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(id));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException | NumberFormatException e) {
            throw new DatabaseException("Error al buscar el usuario con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar los usuarios", e);
        }
        return list;
    }

    @Override
    public boolean updateUser(UserDTO user) {
        String sql = "UPDATE users SET name=?, password=?, state=?, role=? WHERE id_user=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getState());
            statement.setString(4, user.getRole());
            statement.setInt(5, user.getIdUser());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar el usuario con ID: " + user.getIdUser(), e);
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