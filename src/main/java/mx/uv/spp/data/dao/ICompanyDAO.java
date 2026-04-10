package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.CompanyDTO;
import java.util.List;

public interface ICompanyDAO {
    int saveCompany(CompanyDTO company);
    boolean updateCompany(CompanyDTO company);
    CompanyDTO getCompanyById(int companyId);
    List<CompanyDTO> getAllCompanies();
}