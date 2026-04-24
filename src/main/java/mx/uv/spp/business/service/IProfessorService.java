package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

public interface IProfessorService {
    boolean registerProfessor(ProfessorDTO professor) throws DataAccessException;
    boolean inactivateProfessor(int userId) throws DataAccessException;
    boolean existsStaffNumber(String staffNumber) throws DataAccessException;
    int getActiveProfessorsCount() throws DataAccessException;
}