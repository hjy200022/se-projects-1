package us.sep.user.controller;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.log.request.LogRequest;
import us.sep.biz.log.service.LogService;
import us.sep.biz.user.request.UserRegisterRequest;
import us.sep.biz.user.request.UserUpdateRequest;
import us.sep.biz.user.service.UserService;
import us.sep.common.annotion.AvoidRepeatableCommit;
import us.sep.common.annotion.LoggerName;
import us.sep.common.ipUtil;
import us.sep.user.builder.UserBO;
import us.sep.user.builder.UserRoleBO;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;
import us.sep.util.log.LogMark;
import us.sep.util.utils.AssertUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;



/**
 * @Author kana-cr
 * @Date  2020/10/8
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private  UserService userService;

    @Resource
    private LogService logService;

    @AvoidRepeatableCommit
    @PostMapping("/sign-up")
    public Result<UserRegisterRequest> signUp(@RequestBody @Valid UserRegisterRequest userRegisterRequest , HttpServletRequest request) {
        long start = System.currentTimeMillis();
        userService.save(userRegisterRequest);
        logger.info("注册用户：" + userRegisterRequest.getUserName());
        userRegisterRequest.setPassword(null);
        String ip = ipUtil.getIpAddr(request);
        if (StringUtils.isBlank(ip)) {
           ip = LogMark.DEFAULT;
        }
        long end = System.currentTimeMillis();
        LogRequest logRequest = new LogRequest();
        logRequest.setIp(ip);
        logRequest.setIp(ip);
        logRequest.setOperationName("signUp");
        logRequest.setIfSuccess("Y");
        logRequest.setTime(end - start + "ms");
        logRequest.setMessage("注册成功 用户名：" + userRegisterRequest.getUserName());
        logRequest.setUserName(userRegisterRequest.getUserName());
        logService.Save(logRequest);

        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userRegisterRequest);
    }

    @AvoidRepeatableCommit
    @PostMapping("/manager")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public Result<UserRegisterRequest> createManager(@RequestBody @Valid UserRegisterRequest userRegisterRequest , HttpServletRequest request) {
        long start = System.currentTimeMillis();
        userService.saveManager(userRegisterRequest);
        logger.info("注册管理者：" + userRegisterRequest.getUserName());
        userRegisterRequest.setPassword(null);

        String ip = ipUtil.getIpAddr(request);
        if (StringUtils.isBlank(ip)) {
            ip = LogMark.DEFAULT;
        }
        long end = System.currentTimeMillis();
        LogRequest logRequest = new LogRequest();
        logRequest.setIp(ip);
        logRequest.setIp(ip);
        logRequest.setOperationName("signUp");
        logRequest.setIfSuccess("Y");
        logRequest.setTime(end - start + "ms");
        logRequest.setMessage("注册成功 用户名：" + userRegisterRequest.getUserName());
        logRequest.setUserName(userRegisterRequest.getUserName());
        logService.Save(logRequest);

        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userRegisterRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Long> getUserNumber() {
        Long number = userService.getCount();
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),number);
    }


    @GetMapping("/role")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result< List<UserRoleBO>> getUserRole(@RequestParam String userName) {
        List<UserRoleBO> userRole = userService.getUserRole(userName);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userRole);
    }


    @GetMapping("/single")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserBO > getUser(String username) {
        AssertUtil.assertStringNotBlank(username,"用户名不能为空");
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userService.getUser(username));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserBO> > getAllUser(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<UserBO> allUser = userService.getAll(pageNum, pageSize);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),allUser.getContent());
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserUpdateRequest> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        userService.update(userUpdateRequest);
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userUpdateRequest);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> deleteUserByUserName(@RequestParam("username") String username) {
        userService.delete(username);
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),username);
    }


    @DeleteMapping("/deleteInBatch")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<String>> deleteUserByUserNameInBatch(@RequestParam("username") List<String> username) {
        userService.deleteInBatch(username);
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),username);
    }

    @GetMapping("/check")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Boolean> ifManagerOrAdmin(@RequestParam("username") String username){
        return new Result<> (true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),userService.ifManagerOrAdmin(username));
    }



}
