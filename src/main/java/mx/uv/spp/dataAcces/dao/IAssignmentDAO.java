package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.AssignmentDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IAssignmentDAO {
    boolean saveAssignment(AssignmentDTO assignment) throws DataAccessException;
    boolean hasExistingAssignment(int internId) throws DataAccessException;
    boolean inactivateAssignment(int assignmentId) throws DataAccessException;
    AssignmentDTO getAssignmentById(int assignmentId) throws DataAccessException;
    AssignmentDTO getAssignmentByInternId(int internId) throws DataAccessException;
    List<AssignmentDTO> getAssignmentsByProjectId(int projectId) throws DataAccessException;
}