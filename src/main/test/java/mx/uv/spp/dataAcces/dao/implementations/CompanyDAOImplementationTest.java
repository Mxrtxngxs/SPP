package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.CompanyDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CompanyDAOImplementationTest {

    private static final Logger LOGGER = Logger.getLogger(CompanyDAOImplementationTest.class.getName());
    private CompanyDAOImplementation companyDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            companyDAO = new CompanyDAOImplementation();
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Error setting up test database", e);
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            if (connection != null && !connection.isClosed()) {
                String cleanProyecto = "DELETE FROM Proyecto";
                try (PreparedStatement statement = connection.prepareStatement(cleanProyecto)) {
                    statement.executeUpdate();
                }

                String cleanEmpresa = "DELETE FROM Empresa";
                try (PreparedStatement statement = connection.prepareStatement(cleanEmpresa)) {
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up test database", e);
        }
    }

    @Test
    public void saveCompany_NormalFlow_ReturnsGeneratedId() throws DataAccessException {
        CompanyDTO company = createBaseCompany();
        int id = companyDAO.saveCompany(company);
        assertTrue(id > 0);
    }

    @Test
    public void saveCompany_AlternateFlow_NullContact_ReturnsGeneratedId() throws DataAccessException {
        CompanyDTO company = createBaseCompany();
        company.setManagerContact(null);
        int id = companyDAO.saveCompany(company);
        assertTrue(id > 0);
    }

    @Test
    public void saveCompany_ExceptionFlow_NullName_ThrowsException() {
        CompanyDTO company = createBaseCompany();
        company.setName(null);

        assertThrows(DataAccessException.class, () -> {
            companyDAO.saveCompany(company);
        });
    }

    @Test
    public void updateCompany_NormalFlow_ReturnsTrue() throws DataAccessException {
        CompanyDTO company = createBaseCompany();
        int id = companyDAO.saveCompany(company);
        company.setId(id);
        company.setName("Updated Name");

        boolean result = companyDAO.updateCompany(company);
        assertTrue(result);
    }

    @Test
    public void updateCompany_AlternateFlow_NonExistentId_ReturnsFalse() throws DataAccessException {
        CompanyDTO company = createBaseCompany();
        company.setId(99999);

        boolean result = companyDAO.updateCompany(company);
        assertFalse(result);
    }

    @Test
    public void updateCompany_ExceptionFlow_NullSector_ThrowsException() throws DataAccessException {
        CompanyDTO company = createBaseCompany();
        int id = companyDAO.saveCompany(company);
        company.setId(id);
        company.setSector(null);

        assertThrows(DataAccessException.class, () -> {
            companyDAO.updateCompany(company);
        });
    }

    @Test
    public void getCompanyById_NormalFlow_ReturnsCompany() throws DataAccessException {
        CompanyDTO company = createBaseCompany();
        int id = companyDAO.saveCompany(company);

        CompanyDTO result = companyDAO.getCompanyById(id);
        assertNotNull(result);
        assertEquals("Test Company", result.getName());
    }

    @Test
    public void getCompanyById_AlternateFlow_NonExistentId_ReturnsNull() throws DataAccessException {
        CompanyDTO result = companyDAO.getCompanyById(99999);
        assertNull(result);
    }

    @Test
    public void getAllCompanies_NormalFlow_ReturnsList() throws DataAccessException {
        companyDAO.saveCompany(createBaseCompany());
        companyDAO.saveCompany(createBaseCompany());

        List<CompanyDTO> list = companyDAO.getAllCompanies();
        assertFalse(list.isEmpty());
        assertTrue(list.size() >= 2);
    }

    @Test
    public void getAllCompanies_AlternateFlow_EmptyDB_ReturnsEmptyList() throws DataAccessException {
        List<CompanyDTO> list = companyDAO.getAllCompanies();
        assertTrue(list.isEmpty());
    }

    private CompanyDTO createBaseCompany() {
        CompanyDTO company = new CompanyDTO();
        company.setName("Test Company");
        company.setSector("Technology");
        company.setManagerName("John Doe");
        company.setManagerContact("john.doe@test.com");
        return company;
    }
}