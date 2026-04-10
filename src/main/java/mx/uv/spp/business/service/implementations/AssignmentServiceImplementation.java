package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.AssignmentDTO;
import mx.uv.spp.business.service.IAssignmentService;
import mx.uv.spp.data.dao.IAssignmentDAO;
import mx.uv.spp.data.dao.IProjectDAO;

import java.util.Date;
import java.util.List;

public class AssignmentServiceImplementation implements IAssignmentService {

    private final IAssignmentDAO assignmentDAO;
    private final IProjectDAO projectDAO;

    public AssignmentServiceImplementation(IAssignmentDAO assignmentDAO, IProjectDAO projectDAO) {
        this.assignmentDAO = assignmentDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public void assignProject(AssignmentDTO assignment) {
        if (assignmentDAO.hasExistingAssignment(assignment.getInternId())) {
            throw new IllegalStateException("The intern already has an active assignment.");
        }

        assignment.setAssignmentDate(new Date());

        // Valida capacidad y actualiza el proyecto
        if (!projectDAO.incrementAssignedInterns(assignment.getProjectId())) {
            throw new IllegalStateException("The project does not have available capacity.");
        }

        if (!assignmentDAO.saveAssignment(assignment)) {
            throw new RuntimeException("Could not save the assignment.");
        }
    }

    @Override
    public void inactivateAssignment(int assignmentId) {
        if (!assignmentDAO.inactivateAssignment(assignmentId)) {
            throw new RuntimeException("Could not inactivate the assignment.");
        }
    }

    @Override
    public AssignmentDTO getAssignment(int assignmentId) {
        return assignmentDAO.getAssignmentById(assignmentId);
    }

    @Override
    public AssignmentDTO getAssignmentByInternId(int internId) {
        return assignmentDAO.getAssignmentByInternId(internId);
    }

    @Override
    public List<AssignmentDTO> getAssignmentsByProjectId(int projectId) {
        return assignmentDAO.getAssignmentsByProjectId(projectId);
    }
}