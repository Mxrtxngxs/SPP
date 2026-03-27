package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.CompanyDTO;

import java.util.List;

public interface ICompanyDAO {
    boolean saveCompany(CompanyDTO company);
    CompanyDTO findCompanyById(Integer id);
    List<CompanyDTO> findAllCompanies();
    boolean updateCompany(CompanyDTO company);
    boolean deleteCompanyById(Integer id);
}
