package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

public interface IAuthenticationService {
    UserDTO login(String identifier, String password) throws DataAccessException;
}