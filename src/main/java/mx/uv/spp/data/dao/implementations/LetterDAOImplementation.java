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

public class LetterDAOImplementation implements ILetterDAO {

    private final Connection connection;

    private static final String SQL_SAVE_LETTER = "INSERT INTO Oficio (tipo, fecha_generacion, nombre_archivo, id_asignacion) VALUES (?, ?, ?, ?)";
    private static final String SQL_FIND_LETTER_BY_ID = "SELECT * FROM Oficio WHERE id_oficio = ?";
    private static final String SQL_FIND_ALL_LETTERS = "SELECT * FROM Oficio";
    private static final String SQL_UPDATE_LETTER = "UPDATE Oficio SET tipo=?, fecha_generacion=?, nombre_archivo=?, id_asignacion=? WHERE id_oficio=?";
    private static final String SQL_DELETE_LETTER_BY_ID = "DELETE FROM Oficio WHERE id_oficio = ?";

    public LetterDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveLetter(LetterDTO letter) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_LETTER)) {
            statement.setString(1, letter.getType());
            statement.setDate(2, java.sql.Date.valueOf(letter.getGenerationDate()));
            statement.setString(3, letter.getFileName());
            statement.setInt(4, letter.getAssignmentId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar el oficio", e);
        }
    }

    @Override
    public LetterDTO findLetterById(Integer id) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_LETTER_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return mapResultSetToLetter(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar el oficio con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<LetterDTO> findAllLetters() throws DatabaseException {
        List<LetterDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_LETTERS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) list.add(mapResultSetToLetter(resultSet));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar los oficios", e);
        }
        return list;
    }

    @Override
    public boolean updateLetter(LetterDTO letter) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_LETTER)) {
            statement.setString(1, letter.getType());
            statement.setDate(2, java.sql.Date.valueOf(letter.getGenerationDate()));
            statement.setString(3, letter.getFileName());
            statement.setInt(4, letter.getAssignmentId());
            statement.setInt(5, letter.getLetterId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar el oficio con ID: " + letter.getLetterId(), e);
        }
    }

    @Override
    public boolean deleteLetterById(Integer id) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_LETTER_BY_ID)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar el oficio con ID: " + id, e);
        }
    }

    private LetterDTO mapResultSetToLetter(ResultSet resultSet) throws SQLException {
        LetterDTO letter = new LetterDTO();
        letter.setLetterId(resultSet.getInt("id_oficio"));
        letter.setType(resultSet.getString("tipo"));
        letter.setGenerationDate(resultSet.getDate("fecha_generacion").toLocalDate());
        letter.setFileName(resultSet.getString("nombre_archivo"));
        letter.setAssignmentId(resultSet.getInt("id_asignacion"));
        return letter;
    }
}