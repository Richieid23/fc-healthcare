package id.web.fitrarizki.healthcare.service.impl;

import id.web.fitrarizki.healthcare.common.exeption.InvalidPasswordException;
import id.web.fitrarizki.healthcare.dto.AuthReq;
import id.web.fitrarizki.healthcare.dto.UserInfo;
import id.web.fitrarizki.healthcare.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    @Override
    public UserInfo authenticate(AuthReq authReq) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword())
            );

            return (UserInfo) authentication.getPrincipal();
        } catch (Exception e) {
            throw new InvalidPasswordException("Invalid username or password");
        }

    }
}
