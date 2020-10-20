package us.sep.biz.schdule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.exam.entity.ExamDetailDO;
import us.sep.exam.entity.ExamEntryDO;
import us.sep.exam.entity.ExamEntryRecordDO;
import us.sep.exam.entity.ExamRecordDO;
import us.sep.exam.repo.ExamDetailRepo;
import us.sep.exam.repo.ExamEntryRecordRepo;
import us.sep.exam.repo.ExamEntryRepo;
import us.sep.exam.repo.ExamRecordRepo;
import us.sep.util.utils.CollectionUtils;
import us.sep.util.utils.DateUtil;
import us.sep.util.utils.LoggerUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
  * 定时任务实现
  * @Author kana-cr
  * @Date  2020/10/10 20:54
  **/
@Component
public class ScheduleServiceImpl implements  ScheduleService {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Resource
    private ExamDetailRepo examDetailRepo;

    @Resource
    private ExamRecordRepo examRecordRepo;

    @Resource
    private BizIdFactory bizIdFactory;

    @Resource
    private ExamEntryRepo examEntryRepo;

    @Resource
    private ExamEntryRecordRepo examEntryRecordRepo;



    @Override
    @Scheduled(cron = ScheduleConstant.AM_TWO_OF_THE_CLOCK)
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void finishExamDetail() {
        Date now = new Date();
        //过滤没结束的单类考试数据
        List<ExamDetailDO> examDetailDOList = examDetailRepo.findAll().stream().filter(examDetailDO -> DateUtil.parse(examDetailDO.getExamEndTime(), "yyyy年MM月dd日 HH:mm")
                .before(now)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(examDetailDOList)) {
            for (ExamDetailDO examDetailDO : examDetailDOList) {
                examDetailRepo.delete(examDetailDO);
                ExamRecordDO examRecordDO = new ExamRecordDO();
                BeanUtils.copyProperties(examDetailDO, examRecordDO);
                examRecordDO.setExamRecordId(bizIdFactory.getExamRecordId());
                examRecordRepo.save(examRecordDO);
            }
            LoggerUtil.info(LOGGER, "每日考试归档 examDetails={0}", examDetailDOList);
            //help GC
            examDetailDOList = null;
        }
    }

    @Override
    @Scheduled(cron = ScheduleConstant.AM_TWO_OF_THE_CLOCK)
    @Transactional(rollbackFor = Exception.class)
    public void finishExamEntries() {


        //将所有完成或者取消的活动归档
        List<ExamEntryDO> examEntryFinish = examEntryRepo.findByState("FINISH");
        List<ExamEntryDO> examEntryCancel = examEntryRepo.findByState("CANCEL");

        List<ExamEntryDO> dealExamEntryList = new ArrayList<>(examEntryFinish);

        dealExamEntryList.addAll(examEntryCancel);

        //删除完成的报名
        examEntryRepo.deleteInBatch(dealExamEntryList);

        for (ExamEntryDO examEntryDO:dealExamEntryList) {
            ExamEntryRecordDO examEntryRecord = new ExamEntryRecordDO();
            BeanUtils.copyProperties(examEntryDO,examEntryRecord);
            examEntryRecord.setExamEntryRecordId(bizIdFactory.getExamEntryRecordId());
            examEntryRecordRepo.save(examEntryRecord);
            //help GC
            examEntryRecord = null;
        }

        List<String> examEntryIds = dealExamEntryList.stream().map(ExamEntryDO::getExamEntryId).collect(Collectors.toList());
        //help GC
        dealExamEntryList = null;

        //todo 报名用户归档


    }

}
