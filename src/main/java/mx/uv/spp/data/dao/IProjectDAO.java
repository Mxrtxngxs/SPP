package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ProjectDTO;

import java.util.List;

public interface IProjectDAO {
    boolean saveProject(ProjectDTO project);
    ProjectDTO findProjectById(Integer id);
    List<ProjectDTO> findAllProjects();
    boolean updateProject(ProjectDTO project);
    boolean deleteProjectById(Integer id);
}
