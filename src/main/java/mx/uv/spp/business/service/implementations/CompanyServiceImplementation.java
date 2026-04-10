package mx.uv.spp.business.service.implementations;

import mx.uv.spp.business.dto.CompanyDTO;
import mx.uv.spp.business.service.ICompanyService;
import mx.uv.spp.data.dao.ICompanyDAO;

import java.util.List;

public class CompanyServiceImplementation implements ICompanyService {

    private final ICompanyDAO companyDAO;

    public CompanyServiceImplementation(ICompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    @Override
    public int registerCompany(CompanyDTO company) {
        int newCompanyId = companyDAO.saveCompany(company);
        if (newCompanyId == -1) {
            throw new RuntimeException("Could not register the company.");
        }
        return newCompanyId;
    }

    @Override
    public void updateCompany(CompanyDTO company) {
        if (!companyDAO.updateCompany(company)) {
            throw new RuntimeException("Could not update the company.");
        }
    }

    @Override
    public CompanyDTO getCompany(int companyId) {
        return companyDAO.getCompanyById(companyId);
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyDAO.getAllCompanies();
    }
}