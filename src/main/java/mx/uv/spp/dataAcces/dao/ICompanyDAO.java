package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.CompanyDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface ICompanyDAO {
    int saveCompany(CompanyDTO company) throws DataAccessException;
    boolean updateCompany(CompanyDTO company) throws DataAccessException;
    CompanyDTO getCompanyById(int companyId) throws DataAccessException;
    List<CompanyDTO> getAllCompanies() throws DataAccessException;
}