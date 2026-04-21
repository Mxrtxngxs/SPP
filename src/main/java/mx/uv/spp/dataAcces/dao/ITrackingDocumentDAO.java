package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.TrackingDocumentDTO;
import java.util.List;

public interface ITrackingDocumentDAO {
    boolean saveTrackingDocument(TrackingDocumentDTO document);
    boolean updateSignedDocument(int documentId, String signedFileName);
    boolean gradeTrackingDocument(int documentId, double grade, String observations);
    TrackingDocumentDTO getTrackingDocumentById(int documentId);
    List<TrackingDocumentDTO> getDocumentsByInternId(int internId);
    List<TrackingDocumentDTO> getDocumentsByProfessorId(int professorId);
    List<TrackingDocumentDTO> getDocumentsByProjectId(int projectId);
}