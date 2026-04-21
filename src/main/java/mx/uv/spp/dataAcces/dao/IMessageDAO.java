package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.MessageDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IMessageDAO {
    boolean saveMessage(MessageDTO message) throws DataAccessException;
    MessageDTO findMessageById(Integer id) throws DataAccessException;
    List<MessageDTO> findAllMessages() throws DataAccessException;
    boolean updateMessage(MessageDTO message) throws DataAccessException;
    boolean deleteMessageById(Integer id) throws DataAccessException;
}