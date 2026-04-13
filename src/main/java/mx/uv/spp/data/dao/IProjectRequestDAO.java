package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ProjectRequestDTO;
import java.util.List;

public interface IProjectRequestDAO {
    boolean saveProjectRequest(ProjectRequestDTO request);
    boolean updateRequestStatus(int requestId, String status);
    boolean deleteProjectRequest(int requestId);
    ProjectRequestDTO getProjectRequestById(int requestId);
    List<ProjectRequestDTO> getRequestsByInternId(int internId);
    List<ProjectRequestDTO> getRequestsByProjectId(int projectId);
}