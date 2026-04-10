package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.NotificationDTO;
import java.util.List;

public interface INotificationDAO {
    boolean saveNotification(NotificationDTO notification);
    NotificationDTO findNotificationById(Integer id);
    List<NotificationDTO> findAllNotifications();
    boolean updateNotification(NotificationDTO notification);
    boolean deleteNotificationById(Integer id);
}