package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface IUserDAO {

    boolean saveUser(UserDTO user) throws DatabaseException;
    UserDTO findUserById(String id) throws DatabaseException;
    List<UserDTO> findAllUsers() throws DatabaseException;
    boolean updateUser(UserDTO user) throws DatabaseException;

}
