package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

public interface IUserDAO {

    boolean saveCoordinator(CoordinatorDTO coordinator) throws DataAccessException;

    boolean saveProfessor(ProfessorDTO professor) throws DataAccessException;

    boolean saveIntern(InternDTO intern) throws DataAccessException;

    boolean inactivateUser(int userId) throws DataAccessException;

    UserDTO getUserByIdentifier(String identifier) throws DataAccessException;

}