package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.service.IProfessorService;
import mx.uv.spp.data.dao.IProfessorDAO;
import org.mindrot.jbcrypt.BCrypt;

public class ProfessorServiceImplementation implements IProfessorService {

    private final IProfessorDAO professorDAO;

    public ProfessorServiceImplementation(IProfessorDAO professorDAO) {
        this.professorDAO = professorDAO;
    }

    @Override
    public void registerProfessor(ProfessorDTO professor) {
        if (professorDAO.existsStaffNumber(professor.getStaffNumber())) {
            throw new IllegalArgumentException("Staff number is already registered.");
        }

        if (professorDAO.getActiveProfessorsCount() >= 2) {
            throw new IllegalStateException("Maximum limit of 2 active professors reached. Inactivate one first.");
        }

        String encryptedPassword = BCrypt.hashpw(professor.getPassword(), BCrypt.gensalt());
        professor.setPassword(encryptedPassword);

        professor.setState("Activo");

        if (!professorDAO.saveProfessor(professor)) {
            throw new RuntimeException("Could not register the professor. Try again.");
        }
    }
}