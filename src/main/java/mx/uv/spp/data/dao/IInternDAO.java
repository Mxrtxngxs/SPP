package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface IInternDAO {
    boolean existsEnrollmentNumber(String enrollmentNumber) throws DatabaseException;
    boolean saveIntern(InternDTO intern) throws DatabaseException;
    boolean inactivateIntern(int userId) throws DatabaseException;
    InternDTO getInternById(int userId) throws DatabaseException;
    List<InternDTO> getAllInterns() throws DatabaseException;
}