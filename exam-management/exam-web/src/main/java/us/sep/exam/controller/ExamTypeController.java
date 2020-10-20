package us.sep.exam.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.exam.request.ExamTypeRequest;
import us.sep.biz.exam.service.ExamTypeService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.exam.builder.ExamTypeBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/exam")
public class ExamTypeController {

    @Resource
    ExamTypeService examTypeService;

    @GetMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ExamTypeBO>> getExamTypes(ExamTypeRequest request , @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),
                examTypeService.find(request,pageNum,pageSize));

    }

    @AvoidRepeatableCommit
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamTypeBO> CreateExamType(@Valid ExamTypeRequest request){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examTypeService.save(request));
    }

    @AvoidRepeatableCommit
    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ExamTypeBO> UpdateExamType(ExamTypeRequest request){
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examTypeService.update(request));
    }

    @AvoidRepeatableCommit
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteExamType(String examTypeId){
        examTypeService.delete(examTypeId);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),examTypeId);
    }
}
