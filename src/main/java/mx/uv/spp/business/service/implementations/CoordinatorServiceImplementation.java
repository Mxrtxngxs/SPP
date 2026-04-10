package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.CoordinatorDTO;
import mx.uv.spp.business.service.ICoordinatorService;
import mx.uv.spp.data.dao.ICoordinatorDAO;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

public class CoordinatorServiceImplementation implements ICoordinatorService {

    private final ICoordinatorDAO coordinatorDAO;

    public CoordinatorServiceImplementation(ICoordinatorDAO coordinatorDAO) {
        this.coordinatorDAO = coordinatorDAO;
    }

    @Override
    public void registerCoordinator(CoordinatorDTO coordinator) {
        coordinatorDAO.inactivateCurrentCoordinators();

        if (coordinatorDAO.existsStaffNumber(coordinator.getStaffNumber())) {
            throw new IllegalArgumentException("Staff number is already registered.");
        }

        String encryptedPassword = BCrypt.hashpw(coordinator.getPassword(), BCrypt.gensalt());
        coordinator.setPassword(encryptedPassword);
        coordinator.setState("Activo");

        if (!coordinatorDAO.saveCoordinator(coordinator)) {
            throw new RuntimeException("Could not register the coordinator.");
        }
    }

    @Override
    public void inactivateCoordinator(int userId) {
        if (!coordinatorDAO.inactivateCoordinator(userId)) {
            throw new RuntimeException("Could not inactivate the coordinator.");
        }
    }

    @Override
    public CoordinatorDTO getCoordinator(int userId) {
        return coordinatorDAO.getCoordinatorById(userId);
    }

    @Override
    public List<CoordinatorDTO> getAllCoordinators() {
        return coordinatorDAO.getAllCoordinators();
    }
}