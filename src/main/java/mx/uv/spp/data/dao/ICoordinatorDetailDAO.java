package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.CoordinatorDetailDTO;

public interface ICoordinatorDetailDAO {
    boolean existsStaffNumber(String staffNumber);
    boolean inactivateCurrentCoordinators();
    boolean saveCoordinator(CoordinatorDetailDTO coordinator);
}