package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import java.util.List;

public interface ICoordinatorService {
    boolean registerCoordinator(CoordinatorDTO coordinator) throws DataAccessException;
    boolean inactivateCoordinator(int userId) throws DataAccessException;
    List<CoordinatorDTO> getAllCoordinators() throws DataAccessException;
    boolean existsStaffNumber(String staffNumber) throws DataAccessException;
}