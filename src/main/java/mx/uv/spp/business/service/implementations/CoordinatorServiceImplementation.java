package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.service.ICoordinatorService;
import mx.uv.spp.dataAcces.dao.ICoordinatorDAO;
import mx.uv.spp.dataAcces.dao.implementations.CoordinatorDAOImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import java.util.regex.Pattern;

public class CoordinatorServiceImplementation implements ICoordinatorService {
    private final ICoordinatorDAO coordinatorDAO;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{10,}$";

    public CoordinatorServiceImplementation() throws DataAccessException {
        this.coordinatorDAO = new CoordinatorDAOImplementation();
    }

    @Override
    public boolean registerCoordinator(CoordinatorDTO coordinator) throws DataAccessException {
        if (!validatePassword(coordinator.getPassword())) {
            return false;
        }

        if (coordinatorDAO.existsStaffNumber(coordinator.getStaffNumber())) {
            return false;
        }

        String hashedPassword = BCrypt.hashpw(coordinator.getPassword(), BCrypt.gensalt());
        coordinator.setPassword(hashedPassword);

        return coordinatorDAO.saveCoordinator(coordinator);
    }

    @Override
    public boolean inactivateCoordinator(int userId) throws DataAccessException {
        return userId > 0 && coordinatorDAO.inactivateCoordinator(userId);
    }

    @Override
    public List<CoordinatorDTO> getAllCoordinators() throws DataAccessException {
        return coordinatorDAO.getAllCoordinators();
    }

    @Override
    public boolean existsStaffNumber(String staffNumber) throws DataAccessException {
        return coordinatorDAO.existsStaffNumber(staffNumber);
    }

    private boolean validatePassword(String password) {
        return password != null && Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }
}