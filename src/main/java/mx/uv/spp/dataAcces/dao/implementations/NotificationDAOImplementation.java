package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.NotificationDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.INotificationDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAOImplementation implements INotificationDAO {

    private final Connection connection;

    private static final String SQL_SAVE_NOTIFICATION = "INSERT INTO Notificacion (tipo, mensaje, leida, fecha_envio, id_destinatario) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_FIND_NOTIFICATION_BY_ID = "SELECT * FROM Notificacion WHERE id_notificacion = ?";
    private static final String SQL_FIND_ALL_NOTIFICATIONS = "SELECT * FROM Notificacion";
    private static final String SQL_UPDATE_NOTIFICATION = "UPDATE Notificacion SET tipo=?, mensaje=?, leida=?, fecha_envio=?, id_destinatario=? WHERE id_notificacion=?";
    private static final String SQL_DELETE_NOTIFICATION_BY_ID = "DELETE FROM Notificacion WHERE id_notificacion = ?";

    public NotificationDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveNotification(NotificationDTO notification) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE_NOTIFICATION)) {
            statement.setString(1, notification.getType());
            statement.setString(2, notification.getMessage());
            statement.setBoolean(3, notification.getRead());
            statement.setTimestamp(4, new java.sql.Timestamp(notification.getSendDate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()));
            statement.setInt(5, notification.getReceiverId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al guardar la notificacion", e);
        }
    }

    @Override
    public NotificationDTO findNotificationById(Integer id) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_NOTIFICATION_BY_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                return mapResultSetToNotification(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar la notificación con ID: " + id, e);
        }
        return new NotificationDTO(-1);
    }

    @Override
    public List<NotificationDTO> findAllNotifications() throws DataAccessException {
        List<NotificationDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_NOTIFICATIONS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()){
                list.add(mapResultSetToNotification(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar las notificaciones", e);
        }
        return list;
    }

    @Override
    public boolean updateNotification(NotificationDTO notification) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_NOTIFICATION)) {
            statement.setString(1, notification.getType());
            statement.setString(2, notification.getMessage());
            statement.setBoolean(3, notification.getRead());
            statement.setTimestamp(4, new java.sql.Timestamp(notification.getSendDate().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()));
            statement.setInt(5, notification.getReceiverId());
            statement.setInt(6, notification.getNotificationId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar la notificación con ID: " + notification.getNotificationId(), e);
        }
    }

    @Override
    public boolean deleteNotificationById(Integer id) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_NOTIFICATION_BY_ID)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error al eliminar la notificación con ID: " + id, e);
        }
    }

    private NotificationDTO mapResultSetToNotification(ResultSet resultSet) throws SQLException {
        NotificationDTO notification = new NotificationDTO();
        notification.setNotificationId(resultSet.getInt("id_notificacion"));
        notification.setType(resultSet.getString("tipo"));
        notification.setMessage(resultSet.getString("mensaje"));
        notification.setRead(resultSet.getBoolean("leida"));
        notification.setSendDate(resultSet.getTimestamp("fecha_envio").toLocalDateTime());
        notification.setReceiverId(resultSet.getInt("id_destinatario"));
        return notification;
    }
}