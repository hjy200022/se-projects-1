package us.sep.biz.exam.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.exam.request.UserExamEntryRequest;
import us.sep.biz.exam.service.UserExamEntryService;
import us.sep.exam.builder.UserExamEntryBO;
import us.sep.exam.entity.UserExamEntryDO;
import us.sep.exam.repo.UserExamEntryRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserExamEntryServiceImpl implements UserExamEntryService {

    @Resource
    BizIdFactory bizIdFactory;

    @Resource
    UserExamEntryRepo userExamEntryRepo;

    @Override
    public List<UserExamEntryBO> getUserEntryByExamEntryId(String examEntryId) {
        List<UserExamEntryDO> userExamEntryList = userExamEntryRepo.findByExamEntryId(examEntryId);
        return userExamEntryList.stream().map(UserExamEntryDO::ToUserExamEntryBO).collect(Collectors.toList());
    }

    @Override
    public List<UserExamEntryBO> getUserEntryByUserId(String userId) {
        List<UserExamEntryDO> userExamEntryList = userExamEntryRepo.findByUserId(userId);
        return userExamEntryList.stream().map(UserExamEntryDO::ToUserExamEntryBO).collect(Collectors.toList());
    }

    @Override
    public UserExamEntryBO getUserEntryById(String userEntryId) {
        Optional<UserExamEntryDO> optional = userExamEntryRepo.findByUserExamEntryId(userEntryId);
        if (!optional.isPresent())
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该报名信息");

        return optional.get().ToUserExamEntryBO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    //todo redis cache
    public synchronized UserExamEntryBO createUserEntry(UserExamEntryRequest request) {
        UserExamEntryDO userExamEntryDO = new UserExamEntryDO();
        BeanUtils.copyProperties(request,userExamEntryDO);
        userExamEntryDO.setExamEntryId(bizIdFactory.getUserExamEntry());
        return userExamEntryDO.ToUserExamEntryBO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserExamEntryBO> deleteUserEntryByExamEntry(String examEntryId) {
        if (!userExamEntryRepo.existsByExamEntryId(examEntryId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该考试报名信息");
      List<UserExamEntryBO> userExamEntryList = userExamEntryRepo.findByExamEntryId(examEntryId)
              .stream().map(UserExamEntryDO::ToUserExamEntryBO).collect(Collectors.toList());
      userExamEntryRepo.deleteByExamEntryId(examEntryId);
        return userExamEntryList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserExamEntryBO> deleteUserEntryByUserId(String userId) {
        if (!userExamEntryRepo.existsByUserId(userId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该用户的考试报名信息");
        List<UserExamEntryBO> userExamEntryList = userExamEntryRepo.findByUserId(userId)
                .stream().map(UserExamEntryDO::ToUserExamEntryBO).collect(Collectors.toList());
        userExamEntryRepo.deleteByUserId(userId);
        return userExamEntryList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserExamEntryBO deleteUserEntryById(String userEntryId) {
        if (!userExamEntryRepo.existsByUserExamEntryId(userEntryId))
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"不存在该报名信息");

        return userExamEntryRepo.findByUserExamEntryId(userEntryId).get().ToUserExamEntryBO();
    }
}
