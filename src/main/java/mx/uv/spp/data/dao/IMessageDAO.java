package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.MessageDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface IMessageDAO {
    boolean saveMessage(MessageDTO message) throws DatabaseException;
    MessageDTO findMessageById(Integer id) throws DatabaseException;
    List<MessageDTO> findAllMessages() throws DatabaseException;
    boolean updateMessage(MessageDTO message) throws DatabaseException;
    boolean deleteMessageById(Integer id) throws DatabaseException;
}