package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.LetterDTO;
import java.util.List;

public interface ILetterDAO {
    boolean saveLetter(LetterDTO letter);
    LetterDTO findLetterById(Integer id);
    List<LetterDTO> findAllLetters();
    boolean updateLetter(LetterDTO letter);
    boolean deleteLetterById(Integer id);
}