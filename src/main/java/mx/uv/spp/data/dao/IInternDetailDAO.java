package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.InternDTO;
import java.util.List;

public interface IInternDetailDAO {
    boolean saveInternDetail(InternDTO internDetail);
    InternDTO findInternDetailById(Integer id);
    List<InternDTO> findAllInternDetails();
    boolean updateInternDetail(InternDTO internDetail);
    boolean deleteInternDetailById(Integer id);
}