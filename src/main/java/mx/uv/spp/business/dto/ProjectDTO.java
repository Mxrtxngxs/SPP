package mx.uv.spp.business.dto;

import java.util.Date;

public class ProjectDTO {
    private Integer id;
    private String description;
    private Date startDate;
    private Date endDate;
    private Integer internCapacity;
    private Integer assignedInterns;
    private String status;
    private Integer companyId;
    private Integer coordinatorId;

    public ProjectDTO() {
    }

    public ProjectDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getInternCapacity() {
        return internCapacity;
    }

    public void setInternCapacity(Integer internCapacity) {
        this.internCapacity = internCapacity;
    }

    public Integer getAssignedInterns() {
        return assignedInterns;
    }

    public void setAssignedInterns(Integer assignedInterns) {
        this.assignedInterns = assignedInterns;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Integer coordinatorId) {
        this.coordinatorId = coordinatorId;
    }
}
