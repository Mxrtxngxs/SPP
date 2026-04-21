package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.TemplateDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface ITemplateDAO {
    boolean saveTemplate(TemplateDTO template) throws DataAccessException;
    boolean updateTemplate(TemplateDTO template) throws DataAccessException;
    boolean deleteTemplate(int templateId) throws DataAccessException;
    TemplateDTO getTemplateById(int templateId) throws DataAccessException;
    List<TemplateDTO> getAllTemplates() throws DataAccessException;
    List<TemplateDTO> getTemplatesByProfessorId(int professorId) throws DataAccessException;
}