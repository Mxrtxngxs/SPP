package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.business.service.IInternService;
import mx.uv.spp.data.dao.IInternDAO;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

public class InternServiceImplementation implements IInternService {

    private final IInternDAO internDAO;

    public InternServiceImplementation(IInternDAO internDAO) {
        this.internDAO = internDAO;
    }

    @Override
    public void registerIntern(InternDTO intern) {
        if (internDAO.existsEnrollmentNumber(intern.getEnrollmentNumber())) {
            throw new IllegalArgumentException("Enrollment number is already registered.");
        }

        String encryptedPassword = BCrypt.hashpw(intern.getPassword(), BCrypt.gensalt());
        intern.setPassword(encryptedPassword);
        intern.setState("Activo");

        if (!internDAO.saveIntern(intern)) {
            throw new RuntimeException("Could not register the intern.");
        }
    }

    @Override
    public void inactivateIntern(int userId) {
        if (!internDAO.inactivateIntern(userId)) {
            throw new RuntimeException("Could not inactivate the intern.");
        }
    }

    @Override
    public InternDTO getIntern(int userId) {
        return internDAO.getInternById(userId);
    }

    @Override
    public List<InternDTO> getAllInterns() {
        return internDAO.getAllInterns();
    }
}