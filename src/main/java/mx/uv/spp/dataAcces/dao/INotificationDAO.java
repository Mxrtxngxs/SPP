package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.NotificationDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface INotificationDAO {
    boolean saveNotification(NotificationDTO notification) throws DataAccessException;
    NotificationDTO findNotificationById(Integer id) throws DataAccessException;
    List<NotificationDTO> findAllNotifications() throws DataAccessException;
    boolean updateNotification(NotificationDTO notification) throws DataAccessException;
    boolean deleteNotificationById(Integer id) throws DataAccessException;
}