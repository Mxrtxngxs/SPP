package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IUserDAO {

    boolean saveUser(UserDTO user) throws DataAccessException;
    UserDTO findUserById(String id) throws DataAccessException;
    List<UserDTO> findAllUsers() throws DataAccessException;
    boolean updateUser(UserDTO user) throws DataAccessException;

}
