package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.NotificationDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface INotificationDAO {
    boolean saveNotification(NotificationDTO notification) throws DatabaseException;
    NotificationDTO findNotificationById(Integer id) throws DatabaseException;
    List<NotificationDTO> findAllNotifications() throws DatabaseException;
    boolean updateNotification(NotificationDTO notification) throws DatabaseException;
    boolean deleteNotificationById(Integer id) throws DatabaseException;
}