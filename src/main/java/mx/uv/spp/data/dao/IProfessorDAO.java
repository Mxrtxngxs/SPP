package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ProfessorDTO;

public interface IProfessorDAO {
    boolean existsStaffNumber(String staffNumber);
    int getActiveProfessorsCount();
    boolean saveProfessor(ProfessorDTO professor);
}