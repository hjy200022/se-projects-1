package us.sep.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.sep.exam.entity.ExamEntryRecordDO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamEntryRecordRepo extends JpaRepository<ExamEntryRecordDO,Long> {


    Optional<ExamEntryRecordDO> findByExamEntryRecordId(String id);

    List<ExamEntryRecordDO> findByExamEntryId(String activityEntryId);

    List<ExamEntryRecordDO> findByUserId(String useId);

    ExamEntryRecordDO deleteByExamEntryRecordId(String id);

    ExamEntryRecordDO deleteByExamEntryId(String activityEntryId);

    ExamEntryRecordDO deleteByUserId(String userId);
}
