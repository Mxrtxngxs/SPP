package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.ProjectDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IProjectDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements IProjectDAO {

    private final Connection connection;

    public ProjectDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveProject(ProjectDTO project) {
        String sql = "INSERT INTO Proyecto (descripcion, fecha_inicio, fecha_fin, capacidad_practicantes, practicantes_asignados, estado, id_empresa, id_coordinador) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, project.getDescription());
            st.setDate(2, new java.sql.Date(project.getStartDate().getTime()));
            st.setDate(3, new java.sql.Date(project.getEndDate().getTime()));
            st.setInt(4, project.getInternCapacity());
            st.setInt(5, project.getAssignedInterns());
            st.setString(6, project.getStatus());
            st.setInt(7, project.getCompanyId());
            st.setInt(8, project.getCoordinatorId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar el proyecto", e);
        }
    }

    @Override
    public ProjectDTO findProjectById(Integer id) {
        String sql = "SELECT * FROM Proyecto WHERE id_proyecto = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapResultSetToProject(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar el proyecto con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<ProjectDTO> findAllProjects() {
        List<ProjectDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Proyecto";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToProject(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar los proyectos", e);
        }
        return list;
    }

    @Override
    public boolean updateProject(ProjectDTO project) {
        String sql = "UPDATE Proyecto SET descripcion=?, fecha_inicio=?, fecha_fin=?, capacidad_practicantes=?, practicantes_asignados=?, estado=?, id_empresa=?, id_coordinador=? WHERE id_proyecto=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, project.getDescription());
            st.setDate(2, new java.sql.Date(project.getStartDate().getTime()));
            st.setDate(3, new java.sql.Date(project.getEndDate().getTime()));
            st.setInt(4, project.getInternCapacity());
            st.setInt(5, project.getAssignedInterns());
            st.setString(6, project.getStatus());
            st.setInt(7, project.getCompanyId());
            st.setInt(8, project.getCoordinatorId());
            st.setInt(9, project.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar el proyecto con ID: " + project.getId(), e);
        }
    }

    @Override
    public boolean deleteProjectById(Integer id) {
        String sql = "DELETE FROM Proyecto WHERE id_proyecto = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar el proyecto con ID: " + id, e);
        }
    }

    private ProjectDTO mapResultSetToProject(ResultSet rs) throws SQLException {
        ProjectDTO project = new ProjectDTO();
        project.setId(rs.getInt("id_proyecto"));
        project.setDescription(rs.getString("descripcion"));
        project.setStartDate(rs.getDate("fecha_inicio"));
        project.setEndDate(rs.getDate("fecha_fin"));
        project.setInternCapacity(rs.getInt("capacidad_practicantes"));
        project.setAssignedInterns(rs.getInt("practicantes_asignados"));
        project.setStatus(rs.getString("estado"));
        project.setCompanyId(rs.getInt("id_empresa"));
        project.setCoordinatorId(rs.getInt("id_coordinador"));
        return project;
    }
}