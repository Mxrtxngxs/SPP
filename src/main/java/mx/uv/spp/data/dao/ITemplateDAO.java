package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.TemplateDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface ITemplateDAO {
    boolean saveTemplate(TemplateDTO template) throws DatabaseException;
    boolean updateTemplate(TemplateDTO template) throws DatabaseException;
    boolean deleteTemplate(int templateId) throws DatabaseException;
    TemplateDTO getTemplateById(int templateId) throws DatabaseException;
    List<TemplateDTO> getAllTemplates() throws DatabaseException;
    List<TemplateDTO> getTemplatesByProfessorId(int professorId) throws DatabaseException;
}