package id.web.fitrarizki.healthcare.service.impl;

import id.web.fitrarizki.healthcare.common.constant.RoleType;
import id.web.fitrarizki.healthcare.common.exeption.BadRequestException;
import id.web.fitrarizki.healthcare.common.exeption.EmailAlreadyExistsException;
import id.web.fitrarizki.healthcare.common.exeption.UserNotFoundException;
import id.web.fitrarizki.healthcare.common.exeption.UsernameAlreadyExistsException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private CacheService cacheService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private Role role;
    private UserRegisterReq userRegisterReq;

    @BeforeEach
    void setUp() {
        String password = "12345678";
        user = User.builder()
                .id(1L)
                .username("userTest")
                .email("userTest@test.com")
                .password(passwordEncoder.encode(password))
                .enabled(true)
                .build();

        userRegisterReq = UserRegisterReq.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(password)
                .confirmPassword(password)
                .build();

        role = Role.builder()
                .id(1L)
                .name(RoleType.PATIENT)
                .build();
    }

    @Test
    void registerUser_SuccessFullRegister_ReturnUserResponse() {
        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findByName(RoleType.PATIENT)).thenReturn(Optional.of(role));
        when(userRoleRepository.save(any())).thenReturn(UserRole.builder().id(new UserRole.UserRoleId(user.getId(), role.getId())).build());

        UserResponse result = userServiceImpl.registerUser(userRegisterReq);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(1, result.getRoles().size());
        assertEquals(role.getName(), result.getRoles().get(0));
    }

    @Test
    void registerUser_FailedExistUsername_ThrowUsernameAlreadyExistsException() {
        when(userServiceImpl.isUserExistsByUsername(userRegisterReq.getUsername())).thenReturn(Boolean.valueOf(true));

        assertThrows(UsernameAlreadyExistsException.class, () -> userServiceImpl.registerUser(userRegisterReq));
    }

    @Test
    void registerUser_FailedExistEmail_ThrowEmailAlreadyExistsException() {
        when(userServiceImpl.isUserExistsByEmail(userRegisterReq.getEmail())).thenReturn(Boolean.valueOf(true));

        assertThrows(EmailAlreadyExistsException.class, () -> userServiceImpl.registerUser(userRegisterReq));
    }

    @Test
    void registerUser_FailedConfirmPasswordNotMatch_ThrowBadRequestException() {
        userRegisterReq.setConfirmPassword("57493");

        assertThrows(BadRequestException.class, () -> userServiceImpl.registerUser(userRegisterReq));
    }

    @Test
    void getUserById_SuccessFullGet_ReturnUserResponse() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(roleRepository.findByUserId(user.getId())).thenReturn(List.of(role));

        UserResponse result = userServiceImpl.getUser(user.getId());

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(1, result.getRoles().size());
        assertEquals(role.getName(), result.getRoles().get(0));
    }

    @Test
    void getUserById_FailedUserNotFound_ThrowUserNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUser(user.getId()));
    }

    @Test
    void getUserByUsername_SuccessFullGet_ReturnUserResponse() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(roleRepository.findByUserId(user.getId())).thenReturn(List.of(role));

        UserResponse result = userServiceImpl.getUser(user.getUsername());

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(1, result.getRoles().size());
        assertEquals(role.getName(), result.getRoles().get(0));
    }

    @Test
    void getUserByUsername_FailedUserNotFound_ThrowUserNotFoundException() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUser(user.getUsername()));
    }

    @Test
    void updateUser_SuccessFullUpdate_ReturnUserResponse() {
        UserUpdateReq userUpdateReq = UserUpdateReq.builder()
                .username("userTestUpdate")
                .email("userTestUpdate@test.com")
                .currentPassword("12345678")
                .newPassword("87654321")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userServiceImpl.isUserExistsByUsername(userUpdateReq.getUsername())).thenReturn(Boolean.valueOf(false));
        when(userServiceImpl.isUserExistsByEmail(userUpdateReq.getEmail())).thenReturn(Boolean.valueOf(false));
        when(passwordEncoder.matches(userUpdateReq.getCurrentPassword(), user.getPassword())).thenReturn(Boolean.valueOf(true));


        when(userRepository.save(user)).thenReturn(user);

        UserResponse result = userServiceImpl.updateUser(user.getId(), userUpdateReq);

        assertNotNull(result);
        assertEquals(userUpdateReq.getUsername(), result.getUsername());
        assertEquals(userUpdateReq.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_FailedUsernameAlreadyExist_ThrowUsernameAlreadyExistsException() {
        UserUpdateReq userUpdateReq = UserUpdateReq.builder()
                .username("userTestUpdate")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userServiceImpl.isUserExistsByUsername(userUpdateReq.getUsername())).thenReturn(Boolean.valueOf(true));

        assertThrows(UsernameAlreadyExistsException.class, () -> userServiceImpl.updateUser(user.getId(), userUpdateReq));
    }

    @Test
    void updateUser_FailedEmailAlreadyExist_ThrowEmailAlreadyExistsException() {
        UserUpdateReq userUpdateReq = UserUpdateReq.builder()
                .email("userTestUpdate@test.com")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userServiceImpl.isUserExistsByEmail(userUpdateReq.getEmail())).thenReturn(Boolean.valueOf(true));

        assertThrows(EmailAlreadyExistsException.class, () -> userServiceImpl.updateUser(user.getId(), userUpdateReq));
    }

}