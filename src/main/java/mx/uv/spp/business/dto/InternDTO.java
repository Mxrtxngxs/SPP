package mx.uv.spp.business.dto;

public class InternDTO extends UserDTO{
    private Integer userId;
    private String enrollmentNumber;
    private String gender;
    private Boolean speaksIndigenousLanguage;
    private String indigenousLanguage;
    private Integer coordinatorId;
    private Integer professorId;

    public InternDTO() {
    }

    public InternDTO(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    public void setEnrollmentNumber(String enrollmentNumber) {
        this.enrollmentNumber = enrollmentNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getSpeaksIndigenousLanguage() {
        return speaksIndigenousLanguage;
    }

    public void setSpeaksIndigenousLanguage(Boolean speaksIndigenousLanguage) {
        this.speaksIndigenousLanguage = speaksIndigenousLanguage;
    }

    public String getIndigenousLanguage() {
        return indigenousLanguage;
    }

    public void setIndigenousLanguage(String indigenousLanguage) {
        this.indigenousLanguage = indigenousLanguage;
    }

    public Integer getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Integer coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }
}
