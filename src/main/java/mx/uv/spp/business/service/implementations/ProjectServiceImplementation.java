package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.ProjectDTO;
import mx.uv.spp.business.service.IProjectService;
import mx.uv.spp.data.dao.IProjectDAO;

import java.util.List;

public class ProjectServiceImplementation implements IProjectService {

    private final IProjectDAO projectDAO;

    public ProjectServiceImplementation(IProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public void registerProject(ProjectDTO project) {
        validateProjectData(project);
        if (!projectDAO.saveProject(project)) {
            throw new RuntimeException("Could not register the project");
        }
    }

    @Override
    public void updateProject(ProjectDTO project) {
        validateProjectData(project);
        if (!projectDAO.updateProject(project)) {
            throw new RuntimeException("Could not update the projec");
        }
    }

    @Override
    public void deleteProject(int projectId) {
        if (!projectDAO.deleteProject(projectId)) {
            throw new RuntimeException("Could not delete the project");
        }
    }

    @Override
    public ProjectDTO getProject(int projectId) {
        return projectDAO.getProjectById(projectId);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectDAO.getAllProjects();
    }

    @Override
    public List<ProjectDTO> getAvailableProjects() {
        return projectDAO.getAvailableProjects();
    }

    private void validateProjectData(ProjectDTO project) {
        if (project.getInternCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        if (project.getEndDate().before(project.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }
}