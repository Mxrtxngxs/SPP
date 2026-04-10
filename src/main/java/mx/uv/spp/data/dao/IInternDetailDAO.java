package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.InternDetailDTO;
import java.util.List;

public interface IInternDetailDAO {
    boolean saveInternDetail(InternDetailDTO internDetail);
    InternDetailDTO findInternDetailById(Integer id);
    List<InternDetailDTO> findAllInternDetails();
    boolean updateInternDetail(InternDetailDTO internDetail);
    boolean deleteInternDetailById(Integer id);
}