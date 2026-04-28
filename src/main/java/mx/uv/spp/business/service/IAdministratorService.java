package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.UserDTO;

public interface IAdministratorService {
    boolean existsAdmin();
    boolean registerAdmin(UserDTO admin);
}
