package mx.uv.spp.business.dto;

import java.time.LocalDateTime;

public class IndicatorDTO {
    private Integer indicatorId;
    private java.time.LocalDateTime queryDate;
    private String appliedFilters;
    private String fileName;
    private Integer coordinatorId;

    public IndicatorDTO() {
    }

    public IndicatorDTO(Integer indicatorId) {
        this.indicatorId = indicatorId;
    }

    public Integer getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(Integer indicatorId) {
        this.indicatorId = indicatorId;
    }

    public LocalDateTime getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(LocalDateTime queryDate) {
        this.queryDate = queryDate;
    }

    public String getAppliedFilters() {
        return appliedFilters;
    }

    public void setAppliedFilters(String appliedFilters) {
        this.appliedFilters = appliedFilters;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Integer coordinatorId) {
        this.coordinatorId = coordinatorId;
    }
}
