package mx.uv.spp.business.dto;

import java.time.LocalDateTime;

public class SupportDocumentDTO {
    private Integer supportDocumentId;
    private String type;
    private String fileName;
    private java.time.LocalDateTime uploadDate;
    private String status;
    private Integer internId;

    public SupportDocumentDTO(Integer supportDocumentId) {
        this.supportDocumentId = supportDocumentId;
    }

    public Integer getSupportDocumentId() {
        return supportDocumentId;
    }

    public void setSupportDocumentId(Integer supportDocumentId) {
        this.supportDocumentId = supportDocumentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getInternId() {
        return internId;
    }

    public void setInternId(Integer internId) {
        this.internId = internId;
    }
}