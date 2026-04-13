package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.IndicatorDTO;
import java.util.List;

public interface IIndicatorDAO {
    boolean saveIndicator(IndicatorDTO indicator);
    IndicatorDTO getIndicatorById(int indicatorId);
    List<IndicatorDTO> getIndicatorsByCoordinatorId(int coordinatorId);
    List<IndicatorDTO> getAllIndicators();
}