package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

public interface IProfessorDAO {
    boolean existsStaffNumber(String staffNumber) throws DatabaseException;
    int getActiveProfessorsCount() throws DatabaseException;
    boolean saveProfessor(ProfessorDTO professor) throws DatabaseException;
    boolean inactivateProfessor(int userId) throws DatabaseException;
}