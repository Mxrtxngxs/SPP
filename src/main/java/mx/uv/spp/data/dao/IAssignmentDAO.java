package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.AssignmentDTO;
import java.util.List;

public interface IAssignmentDAO {
    boolean saveAssignment(AssignmentDTO assignment);
    boolean hasExistingAssignment(int internId);
    boolean inactivateAssignment(int assignmentId);
    AssignmentDTO getAssignmentById(int assignmentId);
    AssignmentDTO getAssignmentByInternId(int internId);
    List<AssignmentDTO> getAssignmentsByProjectId(int projectId);
}