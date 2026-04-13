package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface ICoordinatorDAO {
    boolean existsStaffNumber(String staffNumber) throws DatabaseException;
    boolean inactivateCurrentCoordinators() throws DatabaseException;
    boolean saveCoordinator(CoordinatorDTO coordinator) throws DatabaseException;
    boolean inactivateCoordinator(int userId) throws DatabaseException;
    CoordinatorDTO getCoordinatorById(int userId) throws DatabaseException;
    List<CoordinatorDTO> getAllCoordinators() throws DatabaseException;
}