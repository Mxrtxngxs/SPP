package mx.uv.spp.data.dao.impl;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IUserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {

    private final Connection connection;

    public UserDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveUser(UserDTO user) {
        String sql = "INSERT INTO users (name, password, state, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getState());
            stmt.setString(4, user.getRole());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al guardar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public UserDTO findUserById(String id) {
        String sql = "SELECT * FROM users WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean updateUser(UserDTO user) {
        String sql = "UPDATE users SET name=?, password=?, state=?, role=? WHERE id_user=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getState());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getIdUser());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    private UserDTO mapResultSetToUser(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setIdUser(rs.getInt("id_user"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setState(rs.getString("state"));
        user.setRole(rs.getString("role"));
        return user;
    }
}