package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.TemplateDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.ITemplateDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TemplateDAOImplementation implements ITemplateDAO {

    private final Connection connection;

    private static final String SQL_SAVE = "INSERT INTO Formato (nombre, tipo_documento, version, nombre_archivo, fecha_subida, id_profesor) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Formato SET nombre=?, tipo_documento=?, version=?, nombre_archivo=?, fecha_subida=?, id_profesor=? WHERE id_formato=?";
    private static final String SQL_DELETE = "DELETE FROM Formato WHERE id_formato=?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Formato WHERE id_formato=?";
    private static final String SQL_FIND_ALL = "SELECT * FROM Formato";
    private static final String SQL_FIND_BY_PROFESSOR = "SELECT * FROM Formato WHERE id_profesor=?";

    public TemplateDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveTemplate(TemplateDTO template) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {
            statement.setString(1, template.getName());
            statement.setString(2, template.getDocumentType());
            statement.setInt(3, template.getVersion());
            statement.setString(4, template.getFileName());
            statement.setTimestamp(5, Timestamp.valueOf(template.getUploadDate()));
            statement.setInt(6, template.getProfessorId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving template", e);
        }
    }

    @Override
    public boolean updateTemplate(TemplateDTO template) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, template.getName());
            statement.setString(2, template.getDocumentType());
            statement.setInt(3, template.getVersion());
            statement.setString(4, template.getFileName());
            statement.setTimestamp(5, Timestamp.valueOf(template.getUploadDate()));
            statement.setInt(6, template.getProfessorId());
            statement.setInt(7, template.getTemplateId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating template ID: " + template.getTemplateId(), e);
        }
    }

    @Override
    public boolean deleteTemplate(int templateId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, templateId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting template ID: " + templateId, e);
        }
    }

    @Override
    public TemplateDTO getTemplateById(int templateId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, templateId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return mapResultSetToTemplate(resultSet);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding template ID: " + templateId, e);
        }
        return new TemplateDTO(-1);
    }

    @Override
    public List<TemplateDTO> getAllTemplates() throws DataAccessException {
        return getTemplateList(SQL_FIND_ALL, null);
    }

    @Override
    public List<TemplateDTO> getTemplatesByProfessorId(int professorId) throws DataAccessException {
        return getTemplateList(SQL_FIND_BY_PROFESSOR, professorId);
    }

    private List<TemplateDTO> getTemplateList(String query, Integer param) throws DataAccessException {
        List<TemplateDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (param != null) {
                statement.setInt(1, param);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapResultSetToTemplate(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing templates", e);
        }
        return list;
    }

    private TemplateDTO mapResultSetToTemplate(ResultSet resultSet) throws SQLException {
        TemplateDTO template = new TemplateDTO();
        template.setTemplateId(resultSet.getInt("id_formato"));
        template.setName(resultSet.getString("nombre"));
        template.setDocumentType(resultSet.getString("tipo_documento"));
        template.setVersion(resultSet.getInt("version"));
        template.setFileName(resultSet.getString("nombre_archivo"));

        Timestamp timestamp = resultSet.getTimestamp("fecha_subida");
        if (timestamp != null) {
            template.setUploadDate(timestamp.toLocalDateTime());
        }

        template.setProfessorId(resultSet.getInt("id_profesor"));
        return template;
    }
}