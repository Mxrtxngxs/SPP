package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.SupportDocumentDTO;
import java.util.List;

public interface ISupportDocumentDAO {
    boolean saveSupportDocument(SupportDocumentDTO document);
    boolean updateSupportDocumentStatus(int documentId, String status);
    boolean deleteSupportDocument(int documentId);
    SupportDocumentDTO getSupportDocumentById(int documentId);
    List<SupportDocumentDTO> getSupportDocumentsByInternId(int internId);
}