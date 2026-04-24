package mx.uv.spp.dataAcces.dao.implementations;

import mx.uv.spp.business.dto.IndicatorDTO;
import mx.uv.spp.dataAcces.config.DatabaseConfig;
import mx.uv.spp.dataAcces.dao.IIndicatorDAO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class IndicatorDAOImplementation implements IIndicatorDAO {

    private final Connection connection;

    private static final String SQL_SAVE = "INSERT INTO Indicador (fecha_consulta, filtros_aplicados, nombre_archivo, id_coordinador) VALUES (?, ?, ?, ?)";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Indicador WHERE id_indicador = ?";
    private static final String SQL_FIND_BY_COORDINATOR = "SELECT * FROM Indicador WHERE id_coordinador = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM Indicador";

    public IndicatorDAOImplementation() throws DataAccessException {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public boolean saveIndicator(IndicatorDTO indicator) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SAVE)) {
            statement.setTimestamp(1, Timestamp.valueOf(indicator.getQueryDate()));
            statement.setString(2, indicator.getAppliedFilters());
            statement.setString(3, indicator.getFileName());
            statement.setInt(4, indicator.getCoordinatorId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error saving indicator", e);
        }
    }

    @Override
    public IndicatorDTO getIndicatorById(int indicatorId) throws DataAccessException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, indicatorId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return mapResultSetToIndicator(resultSet);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding indicator", e);
        }
        return new IndicatorDTO(-1);
    }

    @Override
    public List<IndicatorDTO> getIndicatorsByCoordinatorId(int coordinatorId) throws DataAccessException {
        return getIndicatorList(SQL_FIND_BY_COORDINATOR, coordinatorId);
    }

    @Override
    public List<IndicatorDTO> getAllIndicators() throws DataAccessException {
        return getIndicatorList(SQL_FIND_ALL, null);
    }

    private List<IndicatorDTO> getIndicatorList(String query, Integer coordinatorId) throws DataAccessException {
        List<IndicatorDTO> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (coordinatorId != null) {
                statement.setInt(1, coordinatorId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) list.add(mapResultSetToIndicator(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing indicators", e);
        }
        return list;
    }

    private IndicatorDTO mapResultSetToIndicator(ResultSet rs) throws SQLException {
        IndicatorDTO indicator = new IndicatorDTO();
        indicator.setIndicatorId(rs.getInt("id_indicador"));
        indicator.setQueryDate(rs.getTimestamp("fecha_consulta").toLocalDateTime());
        indicator.setAppliedFilters(rs.getString("filtros_aplicados"));
        indicator.setFileName(rs.getString("nombre_archivo"));
        indicator.setCoordinatorId(rs.getInt("id_coordinador"));
        return indicator;
    }
}