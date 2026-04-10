package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.MessageDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IMessageDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements IMessageDAO {

    private final Connection connection;

    public MessageDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveMessage(MessageDTO message) {
        String sql = "INSERT INTO Mensaje (asunto, cuerpo, fecha_envio, eliminado, id_remitente, id_destinatario) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, message.getSubject());
            st.setString(2, message.getBody());
            st.setTimestamp(3, new java.sql.Timestamp(message.getSendDate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()));
            st.setBoolean(4, message.getDeleted());
            st.setInt(5, message.getSenderId());
            st.setInt(6, message.getReceiverId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar el mensaje", e);
        }
    }

    @Override
    public MessageDTO findMessageById(Integer id) {
        String sql = "SELECT * FROM Mensaje WHERE id_mensaje = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapResultSetToMessage(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar el mensaje con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<MessageDTO> findAllMessages() {
        List<MessageDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Mensaje";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToMessage(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar los mensajes", e);
        }
        return list;
    }

    @Override
    public boolean updateMessage(MessageDTO message) {
        String sql = "UPDATE Mensaje SET asunto=?, cuerpo=?, fecha_envio=?, eliminado=?, id_remitente=?, id_destinatario=? WHERE id_mensaje=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, message.getSubject());
            st.setString(2, message.getBody());
            st.setTimestamp(3, new java.sql.Timestamp(message.getSendDate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()));
            st.setBoolean(4, message.getDeleted());
            st.setInt(5, message.getSenderId());
            st.setInt(6, message.getReceiverId());
            st.setInt(7, message.getMessageId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar el mensaje con ID: " + message.getMessageId(), e);
        }
    }

    @Override
    public boolean deleteMessageById(Integer id) {
        String sql = "DELETE FROM Mensaje WHERE id_mensaje = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar el mensaje con ID: " + id, e);
        }
    }

    private MessageDTO mapResultSetToMessage(ResultSet rs) throws SQLException {
        MessageDTO message = new MessageDTO();
        message.setMessageId(rs.getInt("id_mensaje"));
        message.setSubject(rs.getString("asunto"));
        message.setBody(rs.getString("cuerpo"));
        message.setSendDate(rs.getTimestamp("fecha_envio").toLocalDateTime());
        message.setDeleted(rs.getBoolean("eliminado"));
        message.setSenderId(rs.getInt("id_remitente"));
        message.setReceiverId(rs.getInt("id_destinatario"));
        return message;
    }
}