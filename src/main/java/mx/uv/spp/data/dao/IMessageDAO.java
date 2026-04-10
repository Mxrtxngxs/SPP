package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.MessageDTO;
import java.util.List;

public interface IMessageDAO {
    boolean saveMessage(MessageDTO message);
    MessageDTO findMessageById(Integer id);
    List<MessageDTO> findAllMessages();
    boolean updateMessage(MessageDTO message);
    boolean deleteMessageById(Integer id);
}