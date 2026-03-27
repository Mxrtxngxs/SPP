package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.AssignmentDTO;

import java.util.List;

public interface IAssignmentDAO {
    boolean saveAssignment(AssignmentDTO assignment);
    AssignmentDTO findAssignmentById(Integer id);
    List<AssignmentDTO> findAllAssignments();
    boolean updateAssignment(AssignmentDTO assignment);
    boolean deleteAssignmentById(Integer id);
}
