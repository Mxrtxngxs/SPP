package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.ProfessorDTO;

import java.util.List;

public interface IProfessorService {
    boolean registerProfessor(ProfessorDTO professor);
    boolean inactivateProfessor(int userId);
    boolean existsStaffNumber(String staffNumber);
    int getActiveProfessorsCount();
    List<ProfessorDTO> getActiveProfessors();
}