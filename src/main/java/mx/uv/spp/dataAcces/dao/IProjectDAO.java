package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.ProjectDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IProjectDAO {
    boolean saveProject(ProjectDTO project) throws DataAccessException;
    boolean updateProject(ProjectDTO project) throws DataAccessException;
    boolean deleteProject(int projectId) throws DataAccessException;
    boolean incrementAssignedInterns(int projectId) throws DataAccessException;
    ProjectDTO getProjectById(int projectId) throws DataAccessException;
    List<ProjectDTO> getAllProjects() throws DataAccessException;
    List<ProjectDTO> getAvailableProjects() throws DataAccessException;
}