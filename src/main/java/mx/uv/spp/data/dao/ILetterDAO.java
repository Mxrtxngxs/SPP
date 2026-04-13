package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.LetterDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface ILetterDAO {
    boolean saveLetter(LetterDTO letter) throws DatabaseException;
    LetterDTO findLetterById(Integer id) throws DatabaseException;
    List<LetterDTO> findAllLetters() throws DatabaseException;
    boolean updateLetter(LetterDTO letter) throws DatabaseException;
    boolean deleteLetterById(Integer id) throws DatabaseException;
}