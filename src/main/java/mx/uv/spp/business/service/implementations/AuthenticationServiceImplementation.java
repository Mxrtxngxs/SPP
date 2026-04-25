package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.business.service.IAuthenticationService;
import mx.uv.spp.dataAcces.dao.IUserDAO;
import mx.uv.spp.dataAcces.dao.implementations.UserDAOImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationServiceImplementation implements IAuthenticationService {
    private final IUserDAO userDAO;

    public AuthenticationServiceImplementation() throws DataAccessException {
        this.userDAO = new UserDAOImplementation();
    }

    @Override
    public UserDTO login(String identifier, String password) throws DataAccessException {
        UserDTO user = userDAO.getUserByIdentifier(identifier);

        if (user.getIdUser() != -1 && "Activo".equals(user.getState())) {

            if (BCrypt.checkpw(password, user.getPassword())) {

                user.setPassword(null);
                return user;
            }
        }
        user.setIdUser(-1);
        return user;
    }
}