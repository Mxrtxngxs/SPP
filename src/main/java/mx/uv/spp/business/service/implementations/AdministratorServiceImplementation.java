package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.UserDTO;
import mx.uv.spp.business.service.IAdministratorService;
import mx.uv.spp.dataAcces.dao.IUserDAO;
import mx.uv.spp.dataAcces.dao.implementations.UserDAOImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import mx.uv.spp.utils.LogConfig;
import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class AdministratorServiceImplementation implements IAdministratorService {

    private IUserDAO userDAO;
    private static final Logger LOG = LogConfig.getLogger(AdministratorServiceImplementation.class);
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{10,}$";

    public AdministratorServiceImplementation() {
        try {
            this.userDAO = new UserDAOImplementation();
        } catch (DataAccessException e) {
            LOG.severe("Error al inicializar el DAO: " + e.getMessage());
        }
    }

    @Override
    public boolean existsAdmin() {
        boolean exists = false;

        if (userDAO != null) {
            try {
                exists = userDAO.existsAdmin();
            } catch (DataAccessException e) {
                LOG.severe("Database error checking for admin: " + e.getMessage());
            }
        }

        return exists;
    }

    @Override
    public boolean registerAdmin(UserDTO admin) {
        boolean isRegistered = false;
        if (userDAO != null && admin != null) {
            try {
                if (validatePassword(admin.getPassword())) {
                    admin.setPassword(BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt()));
                    admin.setRole("Administrador");
                    admin.setState("Activo");
                    isRegistered = userDAO.saveAdministrator(admin);
                }
            } catch (DataAccessException e) {
                LOG.severe("Error de BD al registrar admin: " + e.getMessage());
            }
        }
        return isRegistered;
    }

    private boolean validatePassword(String password) {
        return password != null && Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }
}
