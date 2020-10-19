package us.sep.exam.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.exam.request.ExamDetailRequest;
import us.sep.biz.exam.service.ExamDetailService;
import us.sep.common.log.LoggerName;
import us.sep.exam.builder.ExamDetailBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/examDetail")
public class ExamDetailController {

    @Resource
    ExamDetailService examDetailService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamDetailBO>> getAllExamDetail(ExamDetailRequest request , @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examDetailService.find(request,pageNum,pageSize));
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamDetailBO> publishExamDetail(@Valid ExamDetailRequest request){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examDetailService.save(request));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamDetailRequest> updateExamDetail(@Valid ExamDetailRequest request){
        AssertUtil.assertStringNotBlank(request.getExamDetailId(),"考试信息id不能为空");
        examDetailService.update(request);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteExamDetailByDetailId(String examDetailId){
        examDetailService.deleteByExamDetailId(examDetailId);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examDetailId);
    }

    @DeleteMapping("/examType")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteExamDetailByExamTypeId(String examTypeId){
        examDetailService.deleteByExamTypeId(examTypeId);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examTypeId);
    }

}
