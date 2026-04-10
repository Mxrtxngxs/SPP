package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.InternDTO;
import java.util.List;

public interface IInternDAO {
    boolean existsEnrollmentNumber(String enrollmentNumber);
    boolean saveIntern(InternDTO intern);
    boolean inactivateIntern(int userId);
    InternDTO getInternById(int userId);
    List<InternDTO> getAllInterns();
}