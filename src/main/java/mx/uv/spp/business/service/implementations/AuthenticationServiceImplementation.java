package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.business.service.IAuthenticationService;
import mx.uv.spp.dataAcces.dao.IUserDAO;
import mx.uv.spp.dataAcces.dao.implementations.UserDAOImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import mx.uv.spp.utils.LogConfig;
import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Logger;

public class AuthenticationServiceImplementation implements IAuthenticationService {

    private IUserDAO userDAO;
    private static final Logger LOG = LogConfig.getLogger(AuthenticationServiceImplementation.class);

    public AuthenticationServiceImplementation() {
        try {
            this.userDAO = new UserDAOImplementation();
        } catch (DataAccessException e) {
            LOG.severe("Error al inicializar el DAO: " + e.getMessage());
        }
    }

    @Override
    public UserDTO login(String identifier, String password) {
        UserDTO resultUser = new UserDTO();
        resultUser.setIdUser(-1);

        if (userDAO != null) {
            try {
                UserDTO user = userDAO.getUserByIdentifier(identifier);

                if (user.getIdUser() != -1 && "Activo".equals(user.getState())) {
                    if (BCrypt.checkpw(password, user.getPassword())) {
                        user.setPassword(null);
                        resultUser = user;
                    }
                }
            } catch (DataAccessException e) {
                LOG.severe("Database error en login: " + e.getMessage());
            }
        }

        return resultUser;
    }
}