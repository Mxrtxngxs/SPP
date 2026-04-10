package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.CoordinatorDTO;
import java.util.List;

public interface ICoordinatorDAO {
    boolean existsStaffNumber(String staffNumber);
    boolean inactivateCurrentCoordinators();
    boolean saveCoordinator(CoordinatorDTO coordinator);
    boolean inactivateCoordinator(int userId);
    CoordinatorDTO getCoordinatorById(int userId);
    List<CoordinatorDTO> getAllCoordinators();
}