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
import us.sep.exam.entity.ExamRecordDO;
import us.sep.exam.repo.ExamDetailRepo;
import us.sep.exam.repo.ExamRecordRepo;
import us.sep.util.utils.CollectionUtils;
import us.sep.util.utils.DateUtil;
import us.sep.util.utils.LoggerUtil;

import javax.annotation.Resource;
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
    ExamDetailRepo examDetailRepo;
    @Resource
    ExamRecordRepo examRecordRepo;
    @Resource
    private BizIdFactory bizIdFactory;


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
            //GC
            examDetailDOList = null;
        }
    }

    @Override
    //@Scheduled(cron = ScheduleConstant.AM_TWO_OF_THE_CLOCK)
    //@Transactional(rollbackFor = Exception.class)
    public void finishExamEntries() {
        //todo 待完成
    }

}
