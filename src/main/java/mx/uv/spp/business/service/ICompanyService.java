package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.CompanyDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface ICompanyService {
    int registerCompany(CompanyDTO company) throws DataAccessException;
    void updateCompany(CompanyDTO company) throws DataAccessException;
    CompanyDTO getCompany(int companyId) throws DataAccessException;
    List<CompanyDTO> getAllCompanies() throws DataAccessException;
}