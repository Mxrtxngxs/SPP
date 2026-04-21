package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

public interface IProfessorDAO {
    boolean existsStaffNumber(String staffNumber) throws DataAccessException;
    int getActiveProfessorsCount() throws DataAccessException;
    boolean saveProfessor(ProfessorDTO professor) throws DataAccessException;
    boolean inactivateProfessor(int userId) throws DataAccessException;
}