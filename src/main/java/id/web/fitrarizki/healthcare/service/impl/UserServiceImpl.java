package id.web.fitrarizki.healthcare.service.impl;

import id.web.fitrarizki.healthcare.common.constant.RoleType;
import id.web.fitrarizki.healthcare.common.exeption.*;
import id.web.fitrarizki.healthcare.dto.UserRegisterReq;
import id.web.fitrarizki.healthcare.dto.UserResponse;
import id.web.fitrarizki.healthcare.dto.UserUpdateReq;
import id.web.fitrarizki.healthcare.entity.Role;
import id.web.fitrarizki.healthcare.entity.User;
import id.web.fitrarizki.healthcare.entity.UserRole;
import id.web.fitrarizki.healthcare.repository.RoleRepository;
import id.web.fitrarizki.healthcare.repository.UserRepository;
import id.web.fitrarizki.healthcare.repository.UserRoleRepository;
import id.web.fitrarizki.healthcare.service.CacheService;
import id.web.fitrarizki.healthcare.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository  userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;

    private final String USER_CACHE_KEY = "cache:user:";
    private final String USER_ROLES_CACHE_KEY = "cache:user:roles:";

    @Override
    @Transactional
    public UserResponse registerUser(UserRegisterReq userRegisterReq) {
        if (isUserExistsByUsername(userRegisterReq.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists with username: "+userRegisterReq.getUsername());
        }

        if (isUserExistsByEmail(userRegisterReq.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists with email: "+userRegisterReq.getEmail());
        }

        if (!userRegisterReq.getPassword().equals(userRegisterReq.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        User user = User.builder()
                .username(userRegisterReq.getUsername())
                .email(userRegisterReq.getEmail())
                .password(passwordEncoder.encode(userRegisterReq.getPassword()))
                .enabled(true)
                .build();

        user = userRepository.save(user);

        Role role = roleRepository.findByName(RoleType.PATIENT)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        UserRole userRole = UserRole.builder()
                .id(new UserRole.UserRoleId(user.getId(), role.getId()))
                .build();
        userRoleRepository.save(userRole);

        return UserResponse.fromUserAndRoles(user, List.of(role));
    }

    @Override
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        List<Role> roles = roleRepository.findByUserId(user.getId());

        return UserResponse.fromUserAndRoles(user, roles);
    }

    @Override
    public UserResponse getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        List<Role> roles = roleRepository.findByUserId(user.getId());

        return UserResponse.fromUserAndRoles(user, roles);
    }

    @Override
    public Boolean isUserExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean isUserExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateReq userUpdateReq) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        String userCacheKey = USER_CACHE_KEY + user.getUsername();
        String roleCacheKey = USER_ROLES_CACHE_KEY + user.getUsername();

        cacheService.evict(userCacheKey);
        cacheService.evict(roleCacheKey);

        if (userUpdateReq.getUsername() != null && !userUpdateReq.getUsername().equals(user.getUsername())) {
            if (isUserExistsByUsername(userUpdateReq.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already exists with username: "+userUpdateReq.getUsername());
            }

            user.setUsername(userUpdateReq.getUsername());
        }

        if (userUpdateReq.getEmail() != null && !userUpdateReq.getEmail().equals(user.getEmail())) {
            if (isUserExistsByEmail(userUpdateReq.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists with email: "+userUpdateReq.getEmail());
            }

            user.setEmail(userUpdateReq.getEmail());
        }

        if (userUpdateReq.getNewPassword() != null && userUpdateReq.getCurrentPassword() != null) {
            if (passwordEncoder.matches(userUpdateReq.getCurrentPassword(),  user.getPassword())) {
                user.setPassword(passwordEncoder.encode(userUpdateReq.getNewPassword()));
            }
        }

        userRepository.save(user);

        return UserResponse.fromUserAndRoles(user, roleRepository.findByUserId(user.getId()));
    }
}
