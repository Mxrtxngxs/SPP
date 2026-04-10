package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.LetterDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.ILetterDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LetterDAOImpl implements ILetterDAO {

    private final Connection connection;

    public LetterDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveLetter(LetterDTO letter) {
        String sql = "INSERT INTO Oficio (tipo, fecha_generacion, nombre_archivo, id_asignacion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, letter.getType());
            st.setDate(2, java.sql.Date.valueOf(letter.getGenerationDate()));
            st.setString(3, letter.getFileName());
            st.setInt(4, letter.getAssignmentId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar el oficio", e);
        }
    }

    @Override
    public LetterDTO findLetterById(Integer id) {
        String sql = "SELECT * FROM Oficio WHERE id_oficio = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapResultSetToLetter(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar el oficio con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<LetterDTO> findAllLetters() {
        List<LetterDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Oficio";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToLetter(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar los oficios", e);
        }
        return list;
    }

    @Override
    public boolean updateLetter(LetterDTO letter) {
        String sql = "UPDATE Oficio SET tipo=?, fecha_generacion=?, nombre_archivo=?, id_asignacion=? WHERE id_oficio=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, letter.getType());
            st.setDate(2, java.sql.Date.valueOf(letter.getGenerationDate()));
            st.setString(3, letter.getFileName());
            st.setInt(4, letter.getAssignmentId());
            st.setInt(5, letter.getLetterId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar el oficio con ID: " + letter.getLetterId(), e);
        }
    }

    @Override
    public boolean deleteLetterById(Integer id) {
        String sql = "DELETE FROM Oficio WHERE id_oficio = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar el oficio con ID: " + id, e);
        }
    }

    private LetterDTO mapResultSetToLetter(ResultSet rs) throws SQLException {
        LetterDTO letter = new LetterDTO();
        letter.setLetterId(rs.getInt("id_oficio"));
        letter.setType(rs.getString("tipo"));
        letter.setGenerationDate(rs.getDate("fecha_generacion").toLocalDate());
        letter.setFileName(rs.getString("nombre_archivo"));
        letter.setAssignmentId(rs.getInt("id_asignacion"));
        return letter;
    }
}