package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.ProjectDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IProjectService {
    void registerProject(ProjectDTO project) throws DataAccessException;
    void updateProject(ProjectDTO project) throws DataAccessException;
    void deleteProject(int projectId) throws DataAccessException;
    ProjectDTO getProject(int projectId) throws DataAccessException;
    List<ProjectDTO> getAllProjects() throws DataAccessException;
    List<ProjectDTO> getAvailableProjects() throws DataAccessException;
}