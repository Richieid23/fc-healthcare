package id.web.fitrarizki.healthcare.controller;

import id.web.fitrarizki.healthcare.dto.*;
import id.web.fitrarizki.healthcare.service.AuthService;
import id.web.fitrarizki.healthcare.service.JwtService;
import id.web.fitrarizki.healthcare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterReq userRegisterReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRegisterReq));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthRes> login(@Valid @RequestBody AuthReq  authReq) {
        UserInfo userInfo = authService.authenticate(authReq);
        return ResponseEntity.ok(AuthRes.fromUserInfoAndToken(userInfo, jwtService.generateToken(userInfo)));
    }
}
