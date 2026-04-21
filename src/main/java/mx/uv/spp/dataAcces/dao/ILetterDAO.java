package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.LetterDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface ILetterDAO {
    boolean saveLetter(LetterDTO letter) throws DataAccessException;
    LetterDTO findLetterById(Integer id) throws DataAccessException;
    List<LetterDTO> findAllLetters() throws DataAccessException;
    boolean updateLetter(LetterDTO letter) throws DataAccessException;
    boolean deleteLetterById(Integer id) throws DataAccessException;
}