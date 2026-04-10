package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IInternDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

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

    public InternDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean existsEnrollmentNumber(String enrollmentNumber) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_EXISTS_MATRICULA)) {
            stmt.setString(1, enrollmentNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking enrollment number", e);
        }
        return false;
    }

    @Override
    public boolean saveIntern(InternDTO intern) {
        try {
            int generatedId = -1;
            try (PreparedStatement stmtUser = connection.prepareStatement(SQL_SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, intern.getName());
                stmtUser.setString(2, intern.getPassword());
                stmtUser.setString(3, intern.getState());
                stmtUser.executeUpdate();
                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) generatedId = rs.getInt(1);
                }
            }

            if (generatedId != -1) {
                try (PreparedStatement stmtIntern = connection.prepareStatement(SQL_SAVE_INTERN)) {
                    stmtIntern.setInt(1, generatedId);
                    stmtIntern.setString(2, intern.getEnrollmentNumber());
                    stmtIntern.setString(3, intern.getGender());
                    stmtIntern.setBoolean(4, intern.getSpeaksIndigenousLanguage());
                    stmtIntern.setString(5, intern.getIndigenousLanguage());
                    stmtIntern.setObject(6, intern.getCoordinatorId(), Types.INTEGER);
                    stmtIntern.setObject(7, intern.getProfessorId(), Types.INTEGER);
                    return stmtIntern.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving intern", e);
        }
    }

    @Override
    public boolean inactivateIntern(int userId) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_INACTIVATE)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error inactivating intern ID: " + userId, e);
        }
    }

    @Override
    public InternDTO getInternById(int userId) {
        try (PreparedStatement stmt = connection.prepareStatement(SQL_FIND_BY_ID)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToIntern(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding intern", e);
        }
        return null;
    }

    @Override
    public List<InternDTO> getAllInterns() {
        List<InternDTO> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToIntern(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error listing interns", e);
        }
        return list;
    }

    private InternDTO mapResultSetToIntern(ResultSet rs) throws SQLException {
        InternDTO intern = new InternDTO();
        intern.setIdUser(rs.getInt("id_usuario"));
        intern.setUserId(rs.getInt("id_usuario"));
        intern.setName(rs.getString("nombre"));
        intern.setPassword(rs.getString("contrasena"));
        intern.setState(rs.getString("estado"));
        intern.setRole("Practicante");
        intern.setEnrollmentNumber(rs.getString("matricula"));
        intern.setGender(rs.getString("genero"));
        intern.setSpeaksIndigenousLanguage(rs.getBoolean("habla_lengua_indigena"));
        intern.setIndigenousLanguage(rs.getString("lengua_indigena"));
        intern.setCoordinatorId(rs.getObject("id_coordinador") != null ? rs.getInt("id_coordinador") : null);
        intern.setProfessorId(rs.getObject("id_profesor") != null ? rs.getInt("id_profesor") : null);
        return intern;
    }
}