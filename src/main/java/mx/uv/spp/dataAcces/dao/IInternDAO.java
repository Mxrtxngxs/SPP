package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IInternDAO {
    boolean existsEnrollmentNumber(String enrollmentNumber) throws DataAccessException;
    boolean saveIntern(InternDTO intern) throws DataAccessException;
    boolean inactivateIntern(int userId) throws DataAccessException;
    InternDTO getInternById(int userId) throws DataAccessException;
    List<InternDTO> getAllInterns() throws DataAccessException;
}