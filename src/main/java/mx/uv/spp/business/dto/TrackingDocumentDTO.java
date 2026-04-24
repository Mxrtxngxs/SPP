package mx.uv.spp.business.dto;

import java.time.LocalDate;

public class TrackingDocumentDTO {
    private Integer trackingDocumentId;
    private String type;
    private Byte reportNumber;
    private Integer coveredHours;
    private String activities;
    private String month;
    private String criteriaScores;
    private Integer projectId;
    private Double grade;
    private String observations;
    private java.time.LocalDate generationDate;
    private String generatedFileName;
    private String signedFileName;
    private String status;
    private Integer internId;
    private Integer professorId;

    public TrackingDocumentDTO(Integer trackingDocumentId) {
        this.trackingDocumentId = trackingDocumentId;
    }

    public Integer getTrackingDocumentId() {
        return trackingDocumentId;
    }

    public void setTrackingDocumentId(Integer trackingDocumentId) {
        this.trackingDocumentId = trackingDocumentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Byte getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(Byte reportNumber) {
        this.reportNumber = reportNumber;
    }

    public Integer getCoveredHours() {
        return coveredHours;
    }

    public void setCoveredHours(Integer coveredHours) {
        this.coveredHours = coveredHours;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCriteriaScores() {
        return criteriaScores;
    }

    public void setCriteriaScores(String criteriaScores) {
        this.criteriaScores = criteriaScores;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public LocalDate getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDate generationDate) {
        this.generationDate = generationDate;
    }

    public String getGeneratedFileName() {
        return generatedFileName;
    }

    public void setGeneratedFileName(String generatedFileName) {
        this.generatedFileName = generatedFileName;
    }

    public String getSignedFileName() {
        return signedFileName;
    }

    public void setSignedFileName(String signedFileName) {
        this.signedFileName = signedFileName;
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

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }
}