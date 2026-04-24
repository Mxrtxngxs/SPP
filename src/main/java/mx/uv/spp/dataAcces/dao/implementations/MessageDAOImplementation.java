package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.MessageDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IMessageDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImplementation implements IMessageDAO {

    private final Connection connection;

    private static final String SQL_SAVE_MESSAGE = "INSERT INTO Mensaje (asunto, cuerpo, fecha_envio, eliminado, id_remitente, id_destinatario) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_FIND_MESSAGE_BY_ID = "SELECT * FROM Mensaje WHERE id_mensaje = ?";
    private static final String SQL_FIND_ALL_MESSAGES = "SELECT * FROM Mensaje";
    private static final String SQL_UPDATE_MESSAGE = "UPDATE Mensaje SET asunto=?, cuerpo=?, fecha_envio=?, eliminado=?, id_remitente=?, id_destinatario=? WHERE id_mensaje=?";
    private static final String SQL_DELETE_MESSAGE_BY_ID = "DELETE FROM Mensaje WHERE id_mensaje = ?";

    public MessageDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveMessage(MessageDTO message) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_MESSAGE)) {
            statement.setString(1, message.getSubject());
            statement.setString(2, message.getBody());
            statement.setTimestamp(3, new java.sql.Timestamp(message.getSendDate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()));
            statement.setBoolean(4, message.getDeleted());
            statement.setInt(5, message.getSenderId());
            statement.setInt(6, message.getReceiverId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al guardar el mensaje", e);
        }
    }

    @Override
    public MessageDTO findMessageById(Integer id) throws DataAccessException {
        try (PreparedStatement st = connection.prepareStatement(SQL_FIND_MESSAGE_BY_ID)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapResultSetToMessage(rs);
        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar el mensaje con ID: " + id, e);
        }
        return new MessageDTO(-1);
    }

    @Override
    public List<MessageDTO> findAllMessages() throws DataAccessException {
        List<MessageDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_MESSAGES);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) list.add(mapResultSetToMessage(resultSet));
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar los mensajes", e);
        }
        return list;
    }

    @Override
    public boolean updateMessage(MessageDTO message) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_MESSAGE)) {
            statement.setString(1, message.getSubject());
            statement.setString(2, message.getBody());
            statement.setTimestamp(3, new java.sql.Timestamp(message.getSendDate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()));
            statement.setBoolean(4, message.getDeleted());
            statement.setInt(5, message.getSenderId());
            statement.setInt(6, message.getReceiverId());
            statement.setInt(7, message.getMessageId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar el mensaje con ID: " + message.getMessageId(), e);
        }
    }

    @Override
    public boolean deleteMessageById(Integer id) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_MESSAGE_BY_ID)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al eliminar el mensaje con ID: " + id, e);
        }
    }

    private MessageDTO mapResultSetToMessage(ResultSet resultSet) throws SQLException {
        MessageDTO message = new MessageDTO();
        message.setMessageId(resultSet.getInt("id_mensaje"));
        message.setSubject(resultSet.getString("asunto"));
        message.setBody(resultSet.getString("cuerpo"));
        message.setSendDate(resultSet.getTimestamp("fecha_envio").toLocalDateTime());
        message.setDeleted(resultSet.getBoolean("eliminado"));
        message.setSenderId(resultSet.getInt("id_remitente"));
        message.setReceiverId(resultSet.getInt("id_destinatario"));
        return message;
    }
}