package id.web.fitrarizki.healthcare.service.impl;

import id.web.fitrarizki.healthcare.common.constant.RoleType;
import id.web.fitrarizki.healthcare.common.exeption.*;
import id.web.fitrarizki.healthcare.dto.UserRegisterReq;
import id.web.fitrarizki.healthcare.dto.UserResponse;
import id.web.fitrarizki.healthcare.entity.Role;
import id.web.fitrarizki.healthcare.entity.User;
import id.web.fitrarizki.healthcare.entity.UserRole;
import id.web.fitrarizki.healthcare.repository.RoleRepository;
import id.web.fitrarizki.healthcare.repository.UserRepository;
import id.web.fitrarizki.healthcare.repository.UserRoleRepository;
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
}
