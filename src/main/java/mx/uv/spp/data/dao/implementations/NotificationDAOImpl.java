package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.NotificationDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.INotificationDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImpl implements INotificationDAO {

    private final Connection connection;

    public NotificationDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveNotification(NotificationDTO notification) {
        String sql = "INSERT INTO Notificacion (tipo, mensaje, leida, fecha_envio, id_destinatario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, notification.getType());
            st.setString(2, notification.getMessage());
            st.setBoolean(3, notification.getIsRead());
            st.setTimestamp(4, new java.sql.Timestamp(notification.getSendDate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()));
            st.setInt(5, notification.getReceiverId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar la notificación", e);
        }
    }

    @Override
    public NotificationDTO findNotificationById(Integer id) {
        String sql = "SELECT * FROM Notificacion WHERE id_notificacion = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapResultSetToNotification(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar la notificación con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<NotificationDTO> findAllNotifications() {
        List<NotificationDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToNotification(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar las notificaciones", e);
        }
        return list;
    }

    @Override
    public boolean updateNotification(NotificationDTO notification) {
        String sql = "UPDATE Notificacion SET tipo=?, mensaje=?, leida=?, fecha_envio=?, id_destinatario=? WHERE id_notificacion=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, notification.getType());
            st.setString(2, notification.getMessage());
            st.setBoolean(3, notification.getIsRead());
            st.setTimestamp(4, new java.sql.Timestamp(notification.getSendDate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()));
            st.setInt(5, notification.getReceiverId());
            st.setInt(6, notification.getNotificationId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar la notificación con ID: " + notification.getNotificationId(), e);
        }
    }

    @Override
    public boolean deleteNotificationById(Integer id) {
        String sql = "DELETE FROM Notificacion WHERE id_notificacion = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar la notificación con ID: " + id, e);
        }
    }

    private NotificationDTO mapResultSetToNotification(ResultSet rs) throws SQLException {
        NotificationDTO notification = new NotificationDTO();
        notification.setNotificationId(rs.getInt("id_notificacion"));
        notification.setType(rs.getString("tipo"));
        notification.setMessage(rs.getString("mensaje"));
        notification.setIsRead(rs.getBoolean("leida"));
        notification.setSendDate(rs.getTimestamp("fecha_envio").toLocalDateTime());
        notification.setReceiverId(rs.getInt("id_destinatario"));
        return notification;
    }
}