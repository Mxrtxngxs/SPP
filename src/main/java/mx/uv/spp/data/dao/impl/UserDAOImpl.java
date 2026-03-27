package mx.uv.spp.data.dao.impl;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.data.dao.IUserDAO;

import java.util.List;

public class UserDAOImpl implements IUserDAO {
    @Override
    public boolean saveUser(UserDTO user) {
        return false;
    }

    @Override
    public UserDTO findUserById(String id) {
        return null;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return List.of();
    }

    @Override
    public boolean updateUser(UserDTO user) {
        return false;
    }
}
