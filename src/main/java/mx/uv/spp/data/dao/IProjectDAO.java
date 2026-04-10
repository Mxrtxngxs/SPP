package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ProjectDTO;
import java.util.List;

public interface IProjectDAO {
    boolean saveProject(ProjectDTO project);
    boolean updateProject(ProjectDTO project);
    boolean deleteProject(int projectId);
    boolean incrementAssignedInterns(int projectId);
    ProjectDTO getProjectById(int projectId);
    List<ProjectDTO> getAllProjects();
    List<ProjectDTO> getAvailableProjects();
}