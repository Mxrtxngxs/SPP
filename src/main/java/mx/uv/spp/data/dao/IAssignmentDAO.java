package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.AssignmentDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface IAssignmentDAO {
    boolean saveAssignment(AssignmentDTO assignment) throws DatabaseException;
    boolean hasExistingAssignment(int internId) throws DatabaseException;
    boolean inactivateAssignment(int assignmentId) throws DatabaseException;
    AssignmentDTO getAssignmentById(int assignmentId) throws DatabaseException;
    AssignmentDTO getAssignmentByInternId(int internId) throws DatabaseException;
    List<AssignmentDTO> getAssignmentsByProjectId(int projectId) throws DatabaseException;
}