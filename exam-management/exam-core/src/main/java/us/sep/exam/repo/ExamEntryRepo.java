package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.ExamEntryDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamEntryRepo extends JpaRepository<ExamEntryDO,Long> {

    List<ExamEntryDO> findByTerm(String term);

    List<ExamEntryDO> findByUserId(String userId);

    List<ExamEntryDO> findByExamDetailId(String examDetailId);

    Optional<ExamEntryDO> findByExamEntryId(String examEntryId);

    ExamEntryDO deleteByExamDetailId(String examDetailId);

    ExamEntryDO deleteByExamEntryId(String examEntryId);

    ExamEntryDO deleteByUserId(String userId);

}
