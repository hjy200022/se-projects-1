package us.sep.biz.user.service;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.sep.base.idfactory.BizIdFactory;
import us.sep.biz.user.enums.RoleTypeEnum;
import us.sep.biz.user.exception.UserNameAlreadyExistException;
import us.sep.biz.user.exception.UserNameNotFoundException;
import us.sep.biz.user.request.UserRegisterRequest;
import us.sep.biz.user.request.UserUpdateRequest;
import us.sep.user.builder.UserBO;
import us.sep.user.builder.UserRoleBO;
import us.sep.user.entity.Role;
import us.sep.user.entity.User;
import us.sep.user.entity.UserRole;
import us.sep.user.repo.RoleRepo;
import us.sep.user.repo.UserRepo;
import us.sep.user.repo.UserRoleRepo;
import us.sep.util.enums.CommonResultCode;
import us.sep.util.exceptions.CustomizeException;

import javax.annotation.Resource;
import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Slf4j
public class UserService {

    private static final String USERNAME = "username:";

    @Resource
    private UserRepo userRepo;
    @Resource
    private RoleRepo roleRepo;
    @Resource
    private UserRoleRepo userRoleRepo;
    @Resource
    private BizIdFactory bizIdFactory;
    @Resource
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public List<UserRoleBO> getUserRole(String userName){
        User user = null;
       Optional<User> userOptional = userRepo.findByUserName(userName);
       if (userOptional.isPresent()) {
           user = userOptional.get();
       }else {
           return null;
       }
       List<UserRole> userRoleList = userRoleRepo.findByUser(user);
       if (!userRoleList.isEmpty()){
           List<UserRoleBO> list = new ArrayList<>();
           for (UserRole userRole: userRoleList) {
               UserRoleBO userRoleBO = UserRoleBO.builder().roleName(userRole.getRole().getName())
                       .userName(user.getUserName()).fullName(user.getFullName()).build();
               list.add(userRoleBO);
           }

           return list;
       }else {
           return null;
       }
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(UserRegisterRequest userRegisterRequest) {
        ensureUserNameNotExist(userRegisterRequest.getUserName());
        User user = userRegisterRequest.toUser();
        user.setUserId(bizIdFactory.getUserId());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()));
        userRepo.save(user);
        //给用户绑定用户角色
        Role studentRole = null;
        try {
            studentRole = roleRepo.findByName(RoleTypeEnum.USER.getName()).orElseThrow(() -> new RoleNotFoundException("角色不存在") );
        } catch (RoleNotFoundException e) {
            log.info("角色不存在");
        }
        userRoleRepo.save(new UserRole(user, studentRole));
      
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveManager(UserRegisterRequest userRegisterRequest) {
        ensureUserNameNotExist(userRegisterRequest.getUserName());
        User user = userRegisterRequest.toUser();
        user.setUserId(bizIdFactory.getUserId());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()));
        userRepo.save(user);
        //给用户绑定两个角色：用户和管理者
        Role studentRole = null;
        Role managerRole = null;
        try {
            studentRole = roleRepo.findByName(RoleTypeEnum.USER.getName()).orElseThrow(() -> new RoleNotFoundException("角色不存在") );
            managerRole = roleRepo.findByName(RoleTypeEnum.MANAGER.getName()).orElseThrow(() -> new RoleNotFoundException("角色不存在"));
        } catch (RoleNotFoundException e) {
            log.info("角色不存在");
        }
        userRoleRepo.save(new UserRole(user, studentRole));
        userRoleRepo.save(new UserRole(user, managerRole));
    }
    @Transactional(rollbackFor = Exception.class)
    public User find(String userName) {
        return userRepo.findByUserName(userName).orElseThrow(() -> new UserNameNotFoundException(ImmutableMap.of(USERNAME, userName)));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest userUpdateRequest) {
        User user = find(userUpdateRequest.getUserName());
        if (Objects.nonNull(userUpdateRequest.getFullName())) {
            user.setFullName(userUpdateRequest.getFullName());
        }
        if (Objects.nonNull(userUpdateRequest.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(userUpdateRequest.getPassword()));
        }
        if (Objects.nonNull(userUpdateRequest.getEnabled())) {
            user.setEnabled(userUpdateRequest.getEnabled());
        }
        userRepo.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String userName) {
        if (!userRepo.existsByUserName(userName)) {
            throw new UserNameNotFoundException(ImmutableMap.of(USERNAME, userName));
        }
        userRepo.deleteByUserName(userName);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteInBatch(List<String> userNames) {
        for (String userName: userNames) {
            if (!userRepo.existsByUserName(userName)) {
                throw new UserNameNotFoundException(ImmutableMap.of(USERNAME, userName));
            }
            userRepo.deleteByUserName(userName);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public Page<UserBO> getAll(int pageNum, int pageSize) {
        return userRepo.findAll(PageRequest.of(pageNum, pageSize)).map(User::toUserBO);
    }
    @Transactional(rollbackFor = Exception.class)
    public UserBO getUser(String username) {
        Optional<User> optional = userRepo.findByUserName(username);
        if (!optional.isPresent())
            throw new CustomizeException(CommonResultCode.UNFOUNDED,"该用户名不存在");

        return optional.get().toUserBO();
    }
    @Transactional(rollbackFor = Exception.class)
    public Long getCount() {
        return userRepo.count();
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean ifManagerOrAdmin(String username){
       if (!userRepo.existsByUserName(username))
           return false;
        User user = userRepo.findByUserName(username).get();
        for (UserRole userRole:user.getUserRoles()) {
            if (userRole.getRole().getName().equals("MANAGER") || userRole.getRole().getName().equals("ADMIN"))
                return true;
        }
        return false;
    }

    private void ensureUserNameNotExist(String userName) {
        boolean exist = userRepo.findByUserName(userName).isPresent();
        if (exist) {
            throw new UserNameAlreadyExistException(ImmutableMap.of(USERNAME, userName));
        }
    }

}
