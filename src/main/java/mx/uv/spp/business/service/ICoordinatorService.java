package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface ICoordinatorService {
    void registerCoordinator(CoordinatorDTO coordinator) throws DatabaseException;
    void inactivateCoordinator(int userId) throws DatabaseException;
    CoordinatorDTO getCoordinator(int userId) throws DatabaseException;
    List<CoordinatorDTO> getAllCoordinators() throws DatabaseException;
}