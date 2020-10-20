package us.sep.biz.exam.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.sep.biz.exam.request.MessageRequest;
import us.sep.biz.exam.service.MessageService;
import us.sep.exam.entity.ExamTypeDO;
import us.sep.exam.repo.ExamTypeRepo;
import us.sep.message.builder.MessageBO;
import us.sep.message.entity.MessageDO;
import us.sep.message.repo.MessageRepo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    MessageRepo messageRepo;
    @Resource
    ExamTypeRepo examTypeRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MessageRequest request) {
        MessageDO message = new MessageDO();
        BeanUtils.copyProperties(request,message);
        Optional<ExamTypeDO> optionalExamTypeDO = examTypeRepo.findByExamTypeName(request.getExamType());
        if (optionalExamTypeDO.isPresent()) {
            message.setExamTypeId(optionalExamTypeDO.get().getExamTypeId());
            messageRepo.save(message);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(MessageRequest request) {
     List<MessageDO> messageDOS =  findNoPage(request);
     messageRepo.deleteInBatch(messageDOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MessageBO> find(MessageRequest request, int pageNum, int pageSize) {
        MessageDO message = new MessageDO();

        if (!StringUtils.isEmpty(request.getPublisher()))
            message.setPublisher(request.getPublisher());

        if (!StringUtils.isEmpty(request.getChannel()))
            message.setChannel(request.getChannel());

        if (!StringUtils.isEmpty(request.getContent()))
            message.setContent(request.getContent());

        if (!StringUtils.isEmpty(request.getExamType())) {
            Optional<ExamTypeDO> optional = examTypeRepo.findByExamTypeName(request.getExamType());
            optional.ifPresent(examTypeDO -> message.setExamTypeId(examTypeDO.getExamTypeId()));
        }

        if (!StringUtils.isEmpty(request.getExamDescription()))
            message.setExamDescription(request.getExamDescription());

        if (StringUtils.isEmpty(message.getExamTypeId()))
            return messageRepo.findAll(PageRequest.of(pageNum, pageSize)).getContent().stream()
                    .map(MessageDO::ToMessageBO).collect(Collectors.toList());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("publisher", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("channel" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("content" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examDescription" ,ExampleMatcher.GenericPropertyMatchers.contains());
        Example<MessageDO> example = Example.of(message , matcher);
        return messageRepo.findAll(example, PageRequest.of(pageNum, pageSize)).getContent()
                .stream().map(MessageDO::ToMessageBO).peek(temp -> temp.setExamType(request.getExamType())).collect(Collectors.toList());
    }


    private List<MessageDO> findNoPage(MessageRequest request) {
        MessageDO message = new MessageDO();

        if (!StringUtils.isEmpty(request.getPublisher()))
            message.setPublisher(request.getPublisher());

        if (!StringUtils.isEmpty(request.getChannel()))
            message.setChannel(request.getChannel());

        if (!StringUtils.isEmpty(request.getContent()))
            message.setContent(request.getContent());

        if (!StringUtils.isEmpty(request.getExamType())) {
            Optional<ExamTypeDO> optionalExamTypeDO = examTypeRepo.findByExamTypeName(request.getExamType());
            optionalExamTypeDO.ifPresent(examTypeDO -> message.setExamTypeId(examTypeDO.getExamTypeId()));
        }

        if (!StringUtils.isEmpty(request.getExamDescription()))
            message.setExamDescription(request.getExamDescription());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("publisher", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("channel" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("content" ,ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("examDescription" ,ExampleMatcher.GenericPropertyMatchers.contains());
        Example<MessageDO> example = Example.of(message , matcher);
        return messageRepo.findAll(example);
    }

}