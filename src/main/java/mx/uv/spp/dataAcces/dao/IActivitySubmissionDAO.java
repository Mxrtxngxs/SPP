package mx.uv.spp.dataAcces.dao;

import mx.uv.spp.business.dto.ActivitySubmissionDTO;
import mx.uv.spp.dataAcces.exceptions.DataAccessException;

import java.util.List;

public interface IActivitySubmissionDAO {
    boolean saveActivitySubmission(ActivitySubmissionDTO submission) throws DataAccessException;
    ActivitySubmissionDTO findActivitySubmissionById(int id) throws DataAccessException;
    List<ActivitySubmissionDTO> findAllActivitySubmissions() throws DataAccessException;
    boolean updateActivitySubmission(ActivitySubmissionDTO submission) throws DataAccessException;
    boolean deleteActivitySubmissionById(Integer id) throws DataAccessException;
}
