package mx.uv.spp.data.dao;

import mx.uv.spp.business.dto.ActivitySubmissionDTO;

import java.util.List;

public interface IActivitySubmissionDAO {
    boolean saveActivitySubmission(ActivitySubmissionDTO submission);
    ActivitySubmissionDTO findActivitySubmissionById(Integer id);
    List<ActivitySubmissionDTO> findAllActivitySubmissions();
    boolean updateActivitySubmission(ActivitySubmissionDTO submission);
    boolean deleteActivitySubmissionById(Integer id);
}
