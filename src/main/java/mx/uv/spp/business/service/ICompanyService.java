package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.CompanyDTO;
import java.util.List;

public interface ICompanyService {
    int registerCompany(CompanyDTO company);
    void updateCompany(CompanyDTO company);
    CompanyDTO getCompany(int companyId);
    List<CompanyDTO> getAllCompanies();
}