package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.ProfessorDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

public interface IProfessorService {
    void registerProfessor(ProfessorDTO professor) throws DataAccessException;
    void inactivateProfessor(int userId) throws DataAccessException;
}