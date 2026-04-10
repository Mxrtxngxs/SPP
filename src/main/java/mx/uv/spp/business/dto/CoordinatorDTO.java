package mx.uv.spp.business.dto;

public class CoordinatorDTO extends UserDTO {
    private Integer userId;
    private String staffNumber;

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }
}
