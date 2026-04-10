package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InternDetailDAOImpl {

    private final Connection connection;

    public InternDetailDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveInternDetail(InternDTO internDetail) {
        String sql = "INSERT INTO Practicante_Detalle (id_usuario, matricula, sexo, habla_lengua_indigena, lengua_indigena, id_coordinador, id_profesor) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, internDetail.getUserId());
            statement.setString(2, internDetail.getEnrollmentNumber());
            statement.setString(3, internDetail.getGender());
            statement.setBoolean(4, internDetail.getSpeaksIndigenousLanguage());
            statement.setString(5, internDetail.getIndigenousLanguage());
            statement.setInt(6, internDetail.getCoordinatorId());
            statement.setInt(7, internDetail.getProfessorId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar el detalle del practicante", e);
        }
    }

    @Override
    public InternDTO findInternDetailById(Integer id) {
        String sql = "SELECT * FROM Practicante_Detalle WHERE id_usuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return mapResultSetToInternDetail(resultSet);
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar el detalle del practicante con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<InternDTO> findAllInternDetails() {
        List<InternDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Practicante_Detalle";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) list.add(mapResultSetToInternDetail(resultSet));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar los detalles de practicantes", e);
        }
        return list;
    }

    @Override
    public boolean updateInternDetail(InternDTO internDetail) {
        String sql = "UPDATE Practicante_Detalle SET matricula=?, sexo=?, habla_lengua_indigena=?, lengua_indigena=?, id_coordinador=?, id_profesor=? WHERE id_usuario=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, internDetail.getEnrollmentNumber());
            statement.setString(2, internDetail.getGender());
            statement.setBoolean(3, internDetail.getSpeaksIndigenousLanguage());
            statement.setString(4, internDetail.getIndigenousLanguage());
            statement.setInt(5, internDetail.getCoordinatorId());
            statement.setInt(6, internDetail.getProfessorId());
            statement.setInt(7, internDetail.getUserId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar el detalle del practicante con ID: " + internDetail.getUserId(), e);
        }
    }

    @Override
    public boolean deleteInternDetailById(Integer id) {
        String sql = "DELETE FROM Practicante_Detalle WHERE id_usuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar el detalle del practicante con ID: " + id, e);
        }
    }

    private InternDTO mapResultSetToInternDetail(ResultSet resultSet) throws SQLException {
        InternDTO internDetail = new InternDTO();
        internDetail.setUserId(resultSet.getInt("id_usuario"));
        internDetail.setEnrollmentNumber(resultSet.getString("matricula"));
        internDetail.setGender(resultSet.getString("sexo"));
        internDetail.setSpeaksIndigenousLanguage(resultSet.getBoolean("habla_lengua_indigena"));
        internDetail.setIndigenousLanguage(resultSet.getString("lengua_indigena"));
        internDetail.setCoordinatorId(resultSet.getInt("id_coordinador"));
        internDetail.setProfessorId(resultSet.getInt("id_profesor"));
        return internDetail;
    }
}