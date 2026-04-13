package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ActivitySubmissionDTO;
import mx.uv.spp.data.exceptions.DatabaseException;

import java.util.List;

public interface IActivitySubmissionDAO {
    boolean saveActivitySubmission(ActivitySubmissionDTO submission) throws DatabaseException;
    ActivitySubmissionDTO findActivitySubmissionById(int id) throws DatabaseException;
    List<ActivitySubmissionDTO> findAllActivitySubmissions() throws DatabaseException;
    boolean updateActivitySubmission(ActivitySubmissionDTO submission) throws DatabaseException;
    boolean deleteActivitySubmissionById(Integer id) throws DatabaseException;
}
