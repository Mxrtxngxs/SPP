package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.ProjectDTO;
import java.util.List;

public interface IProjectService {
    void registerProject(ProjectDTO project);
    void updateProject(ProjectDTO project);
    void deleteProject(int projectId);
    ProjectDTO getProject(int projectId);
    List<ProjectDTO> getAllProjects();
    List<ProjectDTO> getAvailableProjects();
}