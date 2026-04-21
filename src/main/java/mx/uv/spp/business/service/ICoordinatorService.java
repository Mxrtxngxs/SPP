package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface ICoordinatorService {
    void registerCoordinator(CoordinatorDTO coordinator) throws DataAccessException;
    void inactivateCoordinator(int userId) throws DataAccessException;
    CoordinatorDTO getCoordinator(int userId) throws DataAccessException;
    List<CoordinatorDTO> getAllCoordinators() throws DataAccessException;
}