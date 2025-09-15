package id.web.fitrarizki.healthcare.controller;

import id.web.fitrarizki.healthcare.common.exeption.ForbiddenAccessException;
import id.web.fitrarizki.healthcare.dto.UserInfo;
import id.web.fitrarizki.healthcare.dto.UserResponse;
import id.web.fitrarizki.healthcare.dto.UserUpdateReq;
import id.web.fitrarizki.healthcare.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(){
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(UserResponse.fromUserAndRoles(userInfo.getUser(), userInfo.getRoles()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(name = "id") Long id, @Valid @RequestBody UserUpdateReq userUpdateReq) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userInfo.getUserId().equals(id)) {
            throw new ForbiddenAccessException("Forbidden Access");
        }

        return ResponseEntity.ok(userService.updateUser(id, userUpdateReq));
    }
}
