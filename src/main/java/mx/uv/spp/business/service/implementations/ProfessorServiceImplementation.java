package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.service.IProfessorService;
import mx.uv.spp.dataAcces.dao.IProfessorDAO;
import mx.uv.spp.dataAcces.dao.implementations.ProfessorDAOImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

public class ProfessorServiceImplementation implements IProfessorService {

    private final IProfessorDAO professorDAO;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{10,}$";

    public ProfessorServiceImplementation() throws DataAccessException {
        this.professorDAO = new ProfessorDAOImplementation();
    }

    @Override
    public boolean registerProfessor(ProfessorDTO professor) throws DataAccessException {
        if (!validatePassword(professor.getPassword())) {
            return false;
        }

        if (professorDAO.existsStaffNumber(professor.getStaffNumber())) {
            return false;
        }

        String hashedPassword = BCrypt.hashpw(professor.getPassword(), BCrypt.gensalt());
        professor.setPassword(hashedPassword);

        return professorDAO.saveProfessor(professor);
    }

    @Override
    public boolean inactivateProfessor(int userId) throws DataAccessException {
        return userId > 0 && professorDAO.inactivateProfessor(userId);
    }

    @Override
    public boolean existsStaffNumber(String staffNumber) throws DataAccessException {
        return professorDAO.existsStaffNumber(staffNumber);
    }

    @Override
    public int getActiveProfessorsCount() throws DataAccessException {
        return professorDAO.getActiveProfessorsCount();
    }

    private boolean validatePassword(String password) {
        return password != null && Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }
}