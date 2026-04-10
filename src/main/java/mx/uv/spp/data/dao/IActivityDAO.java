package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ActivityDTO;

import java.util.List;

public interface IActivityDAO {
    boolean saveActivity(ActivityDTO activity);
    boolean updateActivity(ActivityDTO activity);
    boolean deleteActivity(int activityId);
    ActivityDTO getActivityById(int activityId);
    List<ActivityDTO> getActivitiesByProfessorId(int professorId);
    List<ActivityDTO> getActivitiesByProjectId(int projectId);
}
