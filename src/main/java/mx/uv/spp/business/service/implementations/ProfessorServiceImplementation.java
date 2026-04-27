package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.business.service.IProfessorService;
import mx.uv.spp.dataAcces.dao.IProfessorDAO;
import mx.uv.spp.dataAcces.dao.implementations.ProfessorDAOImplementation;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import mx.uv.spp.utils.LogConfig;
import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ProfessorServiceImplementation implements IProfessorService {

    private IProfessorDAO professorDAO;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{10,}$";
    private static final Logger LOG = LogConfig.getLogger(ProfessorServiceImplementation.class);

    public ProfessorServiceImplementation() {
        try {
            this.professorDAO = new ProfessorDAOImplementation();
        } catch (DataAccessException e) {
            LOG.severe("Error al inicializar el DAO: " + e.getMessage());
        }
    }

    @Override
    public boolean registerProfessor(ProfessorDTO professor) {
        boolean isRegistered = false;

        if (professorDAO != null) {
            try {
                if (validatePassword(professor.getPassword())) {
                    if (!professorDAO.existsStaffNumber(professor.getStaffNumber())) {
                        String hashedPassword = BCrypt.hashpw(professor.getPassword(), BCrypt.gensalt());
                        professor.setPassword(hashedPassword);
                        isRegistered = professorDAO.saveProfessor(professor);
                    }
                }
            } catch (DataAccessException e) {
                LOG.severe("Database error registering professor: " + e.getMessage());
            }
        }

        return isRegistered;
    }

    @Override
    public boolean inactivateProfessor(int userId) {
        boolean isInactivated = false;

        if (professorDAO != null) {
            try {
                if (userId > 0) {
                    isInactivated = professorDAO.inactivateProfessor(userId);
                }
            } catch (DataAccessException e) {
                LOG.severe("Database error inactivating professor: " + e.getMessage());
            }
        }

        return isInactivated;
    }

    @Override
    public boolean existsStaffNumber(String staffNumber) {
        boolean exists = false;

        if (professorDAO != null) {
            try {
                exists = professorDAO.existsStaffNumber(staffNumber);
            } catch (DataAccessException e) {
                LOG.severe("Database error checking staff number: " + e.getMessage());
            }
        }

        return exists;
    }

    @Override
    public int getActiveProfessorsCount() {
        int count = 0;

        if (professorDAO != null) {
            try {
                count = professorDAO.getActiveProfessorsCount();
            } catch (DataAccessException e) {
                LOG.severe("Database error getting active professors count: " + e.getMessage());
            }
        }

        return count;
    }

    private boolean validatePassword(String password) {
        boolean isValid = false;

        if (password != null) {
            isValid = Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
        }

        return isValid;
    }
}