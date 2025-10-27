package id.web.fitrarizki.healthcare.service;

import id.web.fitrarizki.healthcare.common.constant.RoleType;
import id.web.fitrarizki.healthcare.dto.UserRegisterReq;
import id.web.fitrarizki.healthcare.dto.UserResponse;
import id.web.fitrarizki.healthcare.dto.UserUpdateReq;

public interface UserService {
    UserResponse registerUser(UserRegisterReq userRegisterReq);
    UserResponse getUser(Long id);
    UserResponse getUser(String username);
    Boolean isUserExistsByUsername(String username);
    Boolean isUserExistsByEmail(String email);
    UserResponse updateUser(Long id, UserUpdateReq userUpdateReq);
    UserResponse grantUserRoles(Long userId, RoleType roleType);
}
