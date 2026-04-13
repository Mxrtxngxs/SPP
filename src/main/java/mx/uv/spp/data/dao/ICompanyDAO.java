package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.CompanyDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface ICompanyDAO {
    int saveCompany(CompanyDTO company) throws DatabaseException;
    boolean updateCompany(CompanyDTO company) throws DatabaseException;
    CompanyDTO getCompanyById(int companyId) throws DatabaseException;
    List<CompanyDTO> getAllCompanies() throws DatabaseException;
}