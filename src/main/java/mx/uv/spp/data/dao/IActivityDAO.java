package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ActivityDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface IActivityDAO {
    boolean saveActivity(ActivityDTO activity) throws DatabaseException;
    boolean updateActivity(ActivityDTO activity) throws DatabaseException;
    boolean deleteActivity(int activityId) throws DatabaseException;
    ActivityDTO getActivityById(int activityId) throws DatabaseException;
    List<ActivityDTO> getActivitiesByProfessorId(int professorId) throws DatabaseException;
    List<ActivityDTO> getActivitiesByProjectId(int projectId) throws DatabaseException;
}
