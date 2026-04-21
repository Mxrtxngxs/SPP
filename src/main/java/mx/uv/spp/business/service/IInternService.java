package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.InternDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IInternService {
    void registerIntern(InternDTO intern) throws DataAccessException;
    void inactivateIntern(int userId) throws DataAccessException;
    InternDTO getIntern(int userId) throws DataAccessException;
    List<InternDTO> getAllInterns() throws DataAccessException;
}