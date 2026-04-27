package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.service.ICoordinatorService;
import mx.uv.spp.dataAcces.dao.ICoordinatorDAO;
import mx.uv.spp.dataAcces.dao.implementations.CoordinatorDAOImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import mx.uv.spp.utils.LogConfig;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class CoordinatorServiceImplementation implements ICoordinatorService {

    private ICoordinatorDAO coordinatorDAO;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{10,}$";
    private static final Logger LOG = LogConfig.getLogger(CoordinatorServiceImplementation.class);

    public CoordinatorServiceImplementation() {
        try {
            this.coordinatorDAO = new CoordinatorDAOImplementation();
        } catch (DataAccessException e) {
            LOG.severe("Error al inicializar el DAO: " + e.getMessage());
        }
    }

    @Override
    public boolean registerCoordinator(CoordinatorDTO coordinator) {
        boolean isRegistered = false;
        if (coordinatorDAO == null) return false;

        try {
            if (validatePassword(coordinator.getPassword())) {
                if (!coordinatorDAO.existsStaffNumber(coordinator.getStaffNumber())) {
                    String hashedPassword = BCrypt.hashpw(coordinator.getPassword(), BCrypt.gensalt());
                    coordinator.setPassword(hashedPassword);
                    isRegistered = coordinatorDAO.saveCoordinator(coordinator);
                }
            }
        } catch (DataAccessException e) {
            LOG.severe("Database error registering coordinator: " + e.getMessage());
        }
        return isRegistered;
    }

    @Override
    public boolean inactivateCoordinator(int userId) {
        boolean isInactivated = false;
        if (coordinatorDAO == null) return false;

        try {
            if (userId > 0) {
                isInactivated = coordinatorDAO.inactivateCoordinator(userId);
            }
        } catch (DataAccessException e) {
            LOG.severe("Database error inactivating coordinator: " + e.getMessage());
        }
        return isInactivated;
    }

    @Override
    public List<CoordinatorDTO> getAllCoordinators() {
        List<CoordinatorDTO> coordinators = new ArrayList<>();
        if (coordinatorDAO == null) return coordinators;

        try {
            coordinators = coordinatorDAO.getAllCoordinators();
        } catch (DataAccessException e) {
            LOG.severe("Database error retrieving coordinators: " + e.getMessage());
        }
        return coordinators;
    }

    @Override
    public List<CoordinatorDTO> getAllActiveCoordinators() {
        List<CoordinatorDTO> coordinators = new ArrayList<>();
        if (coordinatorDAO == null) return coordinators;

        try {
            coordinators = coordinatorDAO.getAllActiveCoordinators();
        } catch (DataAccessException e) {
            LOG.severe("Database error retrieving coordinators: " + e.getMessage());
        }
        return coordinators;
    }

    @Override
    public boolean existsStaffNumber(String staffNumber) {
        boolean exists = false;
        if (coordinatorDAO == null) return false;

        try {
            exists = coordinatorDAO.existsStaffNumber(staffNumber);
        } catch (DataAccessException e) {
            LOG.severe("Database error checking staff number: " + e.getMessage());
        }
        return exists;
    }

    private boolean validatePassword(String password) {
        boolean isValid = false;
        if (password != null) {
            isValid = Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
        }
        return isValid;
    }
}