package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.IndicatorDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IIndicatorDAO {
    boolean saveIndicator(IndicatorDTO indicator) throws DataAccessException;
    IndicatorDTO getIndicatorById(int indicatorId) throws DataAccessException;
    List<IndicatorDTO> getIndicatorsByCoordinatorId(int coordinatorId) throws DataAccessException;
    List<IndicatorDTO> getAllIndicators() throws DataAccessException;
}