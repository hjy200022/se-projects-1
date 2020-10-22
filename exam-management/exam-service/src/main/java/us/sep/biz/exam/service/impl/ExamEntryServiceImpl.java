package us.sep.biz.exam.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.exam.enums.ExamEntryEnum;
import us.sep.biz.exam.request.ExamEntryRequest;
import us.sep.biz.exam.service.ExamEntryService;
import us.sep.exam.builder.ExamEntryBO;
import us.sep.exam.entity.ExamEntryDO;
import us.sep.exam.repo.ExamDetailRepo;
import us.sep.exam.repo.ExamEntryRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamEntryServiceImpl implements ExamEntryService {

    @Resource
    BizIdFactory bizIdFactory;

    @Resource
    ExamEntryRepo examEntryRepo;

    @Resource
    ExamDetailRepo examDetailRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamEntryBO createExamEntry(ExamEntryRequest request) {
        if (!examDetailRepo.existsByExamDetailId(request.getExamDetailId()) )
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该次考试");
        ExamEntryDO examEntryDO = new ExamEntryDO();
        BeanUtils.copyProperties(request,examEntryDO);
        examEntryDO.setState(ExamEntryEnum.Prepare.getState());
        examEntryDO.setNote(ExamEntryEnum.Prepare.getName());
        examEntryDO.setExamEntryId(bizIdFactory.getExamEntryId());
        examEntryRepo.save(examEntryDO);
        return examEntryDO.ToExamEntryBO();
    }

    @Override
    public ExamEntryBO getExamEntry(String examEntryId) {
        Optional<ExamEntryDO> optional = examEntryRepo.findByExamEntryId(examEntryId);
        if (!optional.isPresent())
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该报名消息");
        return optional.get().ToExamEntryBO();
    }

    @Override
    public ExamEntryBO getExamEntryByExamDetail(String examDetailId) {
        Optional<ExamEntryDO> optional = examEntryRepo.findByExamDetailId(examDetailId);
        if (!optional.isPresent())
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该考试");
        return optional.get().ToExamEntryBO();
    }

    @Override
    public List<ExamEntryBO> getExamEntryByTerm(String term) {
        List<ExamEntryDO> examEntryList = examEntryRepo.findByTerm(term);
        return examEntryList.stream().map(ExamEntryDO::ToExamEntryBO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamEntryBO modifyExamEntry(ExamEntryRequest request) {
        Optional<ExamEntryDO> optional = examEntryRepo.findByExamEntryId(request.getExamEntryId());
        if (!optional.isPresent())
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该报名消息");

        ExamEntryDO examEntry = optional.get();

        if (!StringUtils.isEmpty(request.getState())){
            String state = request.getState();
            if (examEntry.getState().equals(ExamEntryEnum.Finish.getState()))
                throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"该报名已经完成，无法更改");

            if (state.equals(ExamEntryEnum.Finish.getState())){
                if (!examEntry.getState().equals(ExamEntryEnum.Start.getState()))
                    throw new CustomizeException(CommonResultCode.ILLEGAL_PARAMETERS,"该报名尚未开始，无法结束");
            }
            examEntry.setState(state);
            for (ExamEntryEnum examEntryEnum:ExamEntryEnum.values()) {
                if (examEntryEnum.getState().equals(state)){
                    examEntry.setNote(examEntryEnum.getName());
                    break;
                }
            }
        }
        if (!StringUtils.isEmpty(request.getContact()))
            examEntry.setContact(request.getContact());

        if (!StringUtils.isEmpty(request.getNumber()))
            examEntry.setNumber(request.getNumber());

        if (!StringUtils.isEmpty(request.getTerm()))
            examEntry.setTerm(request.getTerm());

        examEntryRepo.save(examEntry);
        return examEntry.ToExamEntryBO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamEntryBO deleteExamEntry(String examEntryId) {
        if (examEntryRepo.existsByExamEntryId(examEntryId))
        return examEntryRepo.deleteByExamEntryId(examEntryId).ToExamEntryBO();

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamEntryBO deleteExamEntryByExamDetail(String examDetailId) {
        if (examEntryRepo.existsByExamDetailId(examDetailId))
        return examEntryRepo.deleteByExamDetailId(examDetailId).ToExamEntryBO();

        return null;
    }
}
