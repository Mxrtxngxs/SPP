package mx.uv.spp.data.dao.implementations;

import mx.uv.spp.business.dto.AssignmentDTO;
import mx.uv.spp.data.config.DatabaseConfig;
import mx.uv.spp.data.dao.IAssignmentDAO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAOImpl implements IAssignmentDAO {

    private final Connection connection;

    public AssignmentDAOImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveAssignment(AssignmentDTO assignment) {
        String sql = "INSERT INTO Asignacion (fecha_asignacion, estado, id_practicante, id_proyecto, id_coordinador) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDate(1, new java.sql.Date(assignment.getAssignmentDate().getTime()));
            st.setString(2, assignment.getStatus());
            st.setInt(3, assignment.getInternId());
            st.setInt(4, assignment.getProjectId());
            st.setInt(5, assignment.getCoordinatorId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al guardar la asignación", e);
        }
    }

    @Override
    public AssignmentDTO findAssignmentById(Integer id) {
        String sql = "SELECT * FROM Asignacion WHERE id_asignacion = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return mapResultSetToAssignment(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Error al buscar la asignación con ID: " + id, e);
        }
        return null;
    }

    @Override
    public List<AssignmentDTO> findAllAssignments() {
        List<AssignmentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Asignacion";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) list.add(mapResultSetToAssignment(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Error al listar las asignaciones", e);
        }
        return list;
    }

    @Override
    public boolean updateAssignment(AssignmentDTO assignment) {
        String sql = "UPDATE Asignacion SET fecha_asignacion=?, estado=?, id_practicante=?, id_proyecto=?, id_coordinador=? WHERE id_asignacion=?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDate(1, new java.sql.Date(assignment.getAssignmentDate().getTime()));
            st.setString(2, assignment.getStatus());
            st.setInt(3, assignment.getInternId());
            st.setInt(4, assignment.getProjectId());
            st.setInt(5, assignment.getCoordinatorId());
            st.setInt(6, assignment.getId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al actualizar la asignación con ID: " + assignment.getId(), e);
        }
    }

    @Override
    public boolean deleteAssignmentById(Integer id) {
        String sql = "DELETE FROM Asignacion WHERE id_asignacion = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error al eliminar la asignación con ID: " + id, e);
        }
    }

    private AssignmentDTO mapResultSetToAssignment(ResultSet rs) throws SQLException {
        AssignmentDTO assignment = new AssignmentDTO();
        assignment.setId(rs.getInt("id_asignacion"));
        assignment.setAssignmentDate(rs.getDate("fecha_asignacion"));
        assignment.setStatus(rs.getString("estado"));
        assignment.setInternId(rs.getInt("id_practicante"));
        assignment.setProjectId(rs.getInt("id_proyecto"));
        assignment.setCoordinatorId(rs.getInt("id_coordinador"));
        return assignment;
    }
}