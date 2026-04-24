package mx.uv.spp.business.dto;

import java.time.LocalDate;

public class LetterDTO {
    private Integer letterId;
    private String type;
    private java.time.LocalDate generationDate;
    private String fileName;
    private Integer assignmentId;

    public LetterDTO() {
    }

    public LetterDTO(Integer letterId) {
        this.letterId = letterId;
    }

    public Integer getLetterId() {
        return letterId;
    }

    public void setLetterId(Integer letterId) {
        this.letterId = letterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDate generationDate) {
        this.generationDate = generationDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }
}