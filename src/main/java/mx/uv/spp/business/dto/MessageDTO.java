package mx.uv.spp.business.dto;

public class MessageDTO {
    private Integer messageId;
    private String subject;
    private String body;
    private java.time.LocalDateTime sendDate;
    private Boolean deleted;
    private Integer senderId;
    private Integer receiverId;
}