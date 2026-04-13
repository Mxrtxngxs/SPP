package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ProjectDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface IProjectDAO {
    boolean saveProject(ProjectDTO project) throws DatabaseException;
    boolean updateProject(ProjectDTO project) throws DatabaseException;
    boolean deleteProject(int projectId) throws DatabaseException;
    boolean incrementAssignedInterns(int projectId) throws DatabaseException;
    ProjectDTO getProjectById(int projectId) throws DatabaseException;
    List<ProjectDTO> getAllProjects() throws DatabaseException;
    List<ProjectDTO> getAvailableProjects() throws DatabaseException;
}