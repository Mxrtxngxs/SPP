package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.UserDTO;

import java.util.List;

public interface IUserDAO {

    boolean saveUser(UserDTO user);
    UserDTO findUserById(String id);
    List<UserDTO> findAllUsers();
    boolean updateUser(UserDTO user);

}
