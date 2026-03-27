package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ActivityDTO;

import java.util.List;

public interface IActivityDAO {
    boolean saveActivity(ActivityDTO activity);
    ActivityDTO findActivityById(Integer id);
    List<ActivityDTO> findAllActivities();
    boolean updateActivity(ActivityDTO activity);
    boolean deleteActivityById(Integer id);
}
