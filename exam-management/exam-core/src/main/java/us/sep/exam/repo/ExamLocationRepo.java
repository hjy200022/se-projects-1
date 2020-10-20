package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.ExamLocationDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamLocationRepo extends JpaRepository<ExamLocationDO,Long> {

    List<ExamLocationDO> findByUserId(String userId);

    List<ExamLocationDO> findByExamDetailId(String examDetailId);

    Optional<ExamLocationDO> findByExamLocationId(String examLocationId);

    ExamLocationDO deleteByUserId(String userId);

    ExamLocationDO deleteByExamDetailId(String examDetailId);

    ExamLocationDO deleteByExamLocationId(String examLocationId);

}
