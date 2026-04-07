package mx.uv.spp.business.dto;

public class NotificationDTO {
    private Integer notificationId;
    private String type;
    private String message;
    private Boolean isRead;
    private java.time.LocalDateTime sendDate;
    private Integer receiverId;
}
