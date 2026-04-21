package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.ActivityDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IActivityDAO {
    boolean saveActivity(ActivityDTO activity) throws DataAccessException;
    boolean updateActivity(ActivityDTO activity) throws DataAccessException;
    ActivityDTO getActivityById(int activityId) throws DataAccessException;
    List<ActivityDTO> getActivitiesByProfessorId(int professorId) throws DataAccessException;
}
