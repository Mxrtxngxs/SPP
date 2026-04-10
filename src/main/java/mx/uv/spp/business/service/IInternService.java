package mx.uv.spp.business.service;

import mx.uv.spp.business.dto.InternDTO;
import java.util.List;

public interface IInternService {
    void registerIntern(InternDTO intern);
    void inactivateIntern(int userId);
    InternDTO getIntern(int userId);
    List<InternDTO> getAllInterns();
}