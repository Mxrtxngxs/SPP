package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.CompanyDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.ICompanyDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImpl implements ICompanyDAO {

    private final Connection connection;

    public CompanyDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveCompany(CompanyDTO company) {
        String sql = "INSERT INTO Empresa (nombre_empresa, sector, nombre_responsable, contacto_responsable) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, company.getName());
            st.setString(2, company.getSector());
            st.setString(3, company.getManagerName());
            st.setString(4, company.getManagerContact());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar la empresa", e);
        }
    }

    @Override
    public CompanyDTO findCompanyById(Integer id) {
        String sql = "SELECT * FROM Empresa WHERE id_empresa = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapResultSetToCompany(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar la empresa con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<CompanyDTO> findAllCompanies() {
        List<CompanyDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Empresa";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToCompany(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar las empresas", e);
        }
        return list;
    }

    @Override
    public boolean updateCompany(CompanyDTO company) {
        String sql = "UPDATE Empresa SET nombre_empresa=?, sector=?, nombre_responsable=?, contacto_responsable=? WHERE id_empresa=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, company.getName());
            st.setString(2, company.getSector());
            st.setString(3, company.getManagerName());
            st.setString(4, company.getManagerContact());
            st.setInt(5, company.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar la empresa con ID: " + company.getId(), e);
        }
    }

    @Override
    public boolean deleteCompanyById(Integer id) {
        String sql = "DELETE FROM Empresa WHERE id_empresa = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar la empresa con ID: " + id, e);
        }
    }

    private CompanyDTO mapResultSetToCompany(ResultSet rs) throws SQLException {
        CompanyDTO company = new CompanyDTO();
        company.setId(rs.getInt("id_empresa"));
        company.setName(rs.getString("nombre_empresa"));
        company.setSector(rs.getString("sector"));
        company.setManagerName(rs.getString("nombre_responsable"));
        company.setManagerContact(rs.getString("contacto_responsable"));
        return company;
    }
}