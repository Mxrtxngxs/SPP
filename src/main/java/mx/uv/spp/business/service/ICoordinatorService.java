package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.CoordinatorDTO;
import java.util.List;

public interface ICoordinatorService {
    void registerCoordinator(CoordinatorDTO coordinator);
    void inactivateCoordinator(int userId);
    CoordinatorDTO getCoordinator(int userId);
    List<CoordinatorDTO> getAllCoordinators();
}