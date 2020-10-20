package us.sep.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import us.sep.biz.user.request.RoleCreateRequest;
import us.sep.biz.user.service.RoleService;
import us.sep.common.annotion.LoggerName;
import us.sep.user.entity.Role;
import us.sep.util.common.Result;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.log.Log;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Resource
    RoleService roleService;

    @GetMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<List<Role>> getAllRole() {
        List<Role> roles = roleService.getAllRole();
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),roles);
    }

    @PostMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<RoleCreateRequest> CreateRole(RoleCreateRequest request){
        roleService.save(request);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }

    @PutMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<RoleCreateRequest> updateRole(RoleCreateRequest request){
        roleService.update(request);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }

    @DeleteMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public Result<RoleCreateRequest> deleteRole(RoleCreateRequest request){
        roleService.delete(request);
        return new Result<>(true, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(),request);
    }
}
