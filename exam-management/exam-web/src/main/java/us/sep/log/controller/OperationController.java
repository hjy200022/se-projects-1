package us.sep.log.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.log.request.LogRequest;
import us.sep.biz.log.service.LogService;
import us.sep.common.annotion.LoggerName;
import us.sep.log.builder.LogBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/log")
public class OperationController {

    @Resource
    LogService logService;

    @GetMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<List<LogBO>> getLog(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
       Page<LogBO> logs = logService.getLog(pageNum, pageSize);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),logs.getContent());
    }

    @GetMapping("/condition")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<List<LogBO>> getLogByCondition(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize ,
                                                LogRequest logRequest){
        AssertUtil.assertNotNull(logRequest);
        List<LogBO> logs = logService.getLogByCondition(pageNum, pageSize ,logRequest);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),logs);
    }
}
