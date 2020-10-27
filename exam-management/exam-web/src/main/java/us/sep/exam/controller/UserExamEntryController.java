package us.sep.exam.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.exam.request.UserExamEntryRequest;
import us.sep.biz.exam.service.UserExamEntryRecordService;
import us.sep.biz.exam.service.UserExamEntryService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.exam.builder.UserExamEntryBO;
import us.sep.exam.builder.UserExamEntryRecordBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/userExamEntry")
public class UserExamEntryController {

    @Resource
    UserExamEntryService userExamEntryService;

    @Resource
    UserExamEntryRecordService userExamEntryRecordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserExamEntryBO>> getUserEntryByExamEntry(String examEntryId){
        AssertUtil.assertStringNotBlank(examEntryId,"考试报名信息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryService.getUserEntryByExamEntryId(examEntryId));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserExamEntryBO>> getUserEntry(String userId){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryService.getUserEntryByUserId(userId));
    }

    @GetMapping("/single")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserExamEntryBO> getUserEntryById(String userExamEntryId){
        AssertUtil.assertStringNotBlank(userExamEntryId,"用户报名id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryService.getUserEntryById(userExamEntryId));
    }

    @GetMapping("/recordByExam")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserExamEntryRecordBO>> getUserEntryRecordByExamEntry(String examEntryId){
        AssertUtil.assertStringNotBlank(examEntryId,"报名信息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryRecordService.getUserExamEntryRecordByExamEntry(examEntryId));
    }


    @GetMapping("/recordByUser")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserExamEntryRecordBO>> getUserEntryRecordByUserId(String userId){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryRecordService.getUserExamEntryRecordByUserId(userId));
    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserExamEntryBO> createUserEntryById(@Valid UserExamEntryRequest request){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryService.createUserEntry(request));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserExamEntryBO>> deleteUserEntryByExamEntry(String examEntryId){
        AssertUtil.assertStringNotBlank(examEntryId,"考试报名信息id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryService.deleteUserEntryByExamEntry(examEntryId));
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserExamEntryBO>> deleteUserEntry(String userId){
        AssertUtil.assertStringNotBlank(userId,"用户id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryService.deleteUserEntryByUserId(userId));
    }


    @DeleteMapping("/single")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserExamEntryBO> DeleteUserEntryById(String userExamEntryId){
        AssertUtil.assertStringNotBlank(userExamEntryId,"用户报名id不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userExamEntryService.deleteUserEntryById(userExamEntryId));
    }









}
