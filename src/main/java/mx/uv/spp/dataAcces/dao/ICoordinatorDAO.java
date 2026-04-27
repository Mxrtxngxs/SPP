package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface ICoordinatorDAO {
    boolean existsStaffNumber(String staffNumber) throws DataAccessException;
    boolean inactivateCurrentCoordinators() throws DataAccessException;
    boolean saveCoordinator(CoordinatorDTO coordinator) throws DataAccessException;
    boolean inactivateCoordinator(int userId) throws DataAccessException;
    CoordinatorDTO getCoordinatorById(int userId) throws DataAccessException;
    List<CoordinatorDTO> getAllCoordinators() throws DataAccessException;
    List<CoordinatorDTO> getActiveCoordinators() throws DataAccessException;

    List<CoordinatorDTO> getAllActiveCoordinators() throws DataAccessException;
}