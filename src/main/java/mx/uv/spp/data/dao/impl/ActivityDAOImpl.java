package mx.uv.spp.data.dao.impl;

import mx.uv.spp.business.dto.ActivityDTO;
import mx.uv.spp.data.dao.IActivityDAO;

import java.util.List;

public class ActivityDAOImpl implements IActivityDAO {
    @Override
    public boolean saveActivity(ActivityDTO activity) {
        return false;
    }

    @Override
    public ActivityDTO findActivityById(Integer id) {
        return null;
    }

    @Override
    public List<ActivityDTO> findAllActivities() {
        return List.of();
    }

    @Override
    public boolean updateActivity(ActivityDTO activity) {
        return false;
    }

    @Override
    public boolean deleteActivityById(Integer id) {
        return false;
    }
}
