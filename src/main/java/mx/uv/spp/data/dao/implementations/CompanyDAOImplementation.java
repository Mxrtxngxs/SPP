package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.CompanyDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.ICompanyDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImplementation implements ICompanyDAO {

    private final Connection connection;

    private static final String SQL_SAVE = "INSERT INTO Empresa (nombre, sector, nombre_responsable, contacto_responsable) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Empresa SET nombre=?, sector=?, nombre_responsable=?, contacto_responsable=? WHERE id_empresa=?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Empresa WHERE id_empresa=?";
    private static final String SQL_FIND_ALL = "SELECT * FROM Empresa";

    public CompanyDAOImplementation() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public int saveCompany(CompanyDTO company) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, company.getName());
            statement.setString(2, company.getSector());
            statement.setString(3, company.getManagerName());
            statement.setString(4, company.getManagerContact());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error saving company", e);
        }
        return -1;
    }

    @Override
    public boolean updateCompany(CompanyDTO company) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, company.getName());
            statement.setString(2, company.getSector());
            statement.setString(3, company.getManagerName());
            statement.setString(4, company.getManagerContact());
            statement.setInt(5, company.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating company ID: " + company.getId(), e);
        }
    }

    @Override
    public CompanyDTO getCompanyById(int companyId) throws DatabaseException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, companyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCompany(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding company", e);
        }
        return null;
    }

    @Override
    public List<CompanyDTO> getAllCompanies() throws DatabaseException {
        List<CompanyDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(mapResultSetToCompany(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing companies", e);
        }
        return list;
    }

    private CompanyDTO mapResultSetToCompany(ResultSet resultSet) throws SQLException {
        CompanyDTO company = new CompanyDTO();
        company.setId(resultSet.getInt("id_empresa"));
        company.setName(resultSet.getString("nombre"));
        company.setSector(resultSet.getString("sector"));
        company.setManagerName(resultSet.getString("nombre_responsable"));
        company.setManagerContact(resultSet.getString("contacto_responsable"));
        return company;
    }
}