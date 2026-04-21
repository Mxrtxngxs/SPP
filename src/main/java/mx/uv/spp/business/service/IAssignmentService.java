package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.AssignmentDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IAssignmentService {
    void assignProject(AssignmentDTO assignment) throws DataAccessException;
    void inactivateAssignment(int assignmentId) throws DataAccessException;
    AssignmentDTO getAssignment(int assignmentId) throws DataAccessException;
    AssignmentDTO getAssignmentByInternId(int internId) throws DataAccessException;
    List<AssignmentDTO> getAssignmentsByProjectId(int projectId) throws DataAccessException;
}