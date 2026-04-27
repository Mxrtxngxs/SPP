package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.CoordinatorDTO;
import java.util.List;

public interface ICoordinatorService {
    boolean registerCoordinator(CoordinatorDTO coordinator);
    boolean inactivateCoordinator(int userId);
    List<CoordinatorDTO> getAllCoordinators();
    List<CoordinatorDTO> getAllActiveCoordinators();
    boolean existsStaffNumber(String staffNumber);
}