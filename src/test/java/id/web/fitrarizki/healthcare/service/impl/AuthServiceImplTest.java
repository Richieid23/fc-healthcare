package id.web.fitrarizki.healthcare.service.impl;

import id.web.fitrarizki.healthcare.common.constant.RoleType;
import id.web.fitrarizki.healthcare.common.exeption.InvalidPasswordException;
import id.web.fitrarizki.healthcare.dto.AuthReq;
import id.web.fitrarizki.healthcare.dto.UserInfo;
import id.web.fitrarizki.healthcare.entity.Role;
import id.web.fitrarizki.healthcare.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private AuthReq authReq;
    private UserInfo userInfo;

    @BeforeEach
    void setUp() {
        authReq = new AuthReq("userTest", "password");
        userInfo = new UserInfo(
                User.builder().username("userTest").build(),
                List.of(Role.builder().name(RoleType.PATIENT).build())
        );
    }

    @Test
    void authenticate_SuccessFullAuthentication_ReturnUserInfo() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, null);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        UserInfo result = authServiceImpl.authenticate(authReq);

        assertNotNull(result);
        assertEquals(authReq.getUsername(), result.getUsername());
    }

    @Test
    void authenticate_FailedAuthentication_ThrowInvalidPasswordException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication Failed"));

        assertThrows(InvalidPasswordException.class,  () -> authServiceImpl.authenticate(authReq));
    }
}