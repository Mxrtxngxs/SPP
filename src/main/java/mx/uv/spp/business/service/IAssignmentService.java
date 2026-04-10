package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.AssignmentDTO;
import java.util.List;

public interface IAssignmentService {
    void assignProject(AssignmentDTO assignment);
    void inactivateAssignment(int assignmentId);
    AssignmentDTO getAssignment(int assignmentId);
    AssignmentDTO getAssignmentByInternId(int internId);
    List<AssignmentDTO> getAssignmentsByProjectId(int projectId);
}