package id.web.fitrarizki.healthcare.controller.admin;

import id.web.fitrarizki.healthcare.dto.GrantUserRoleReq;
import id.web.fitrarizki.healthcare.dto.UserResponse;
import id.web.fitrarizki.healthcare.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class UserController {

    private final UserService userService;

    @PostMapping("/grent")
    public ResponseEntity<UserResponse> grantRole(@Valid @RequestBody GrantUserRoleReq grantUserRoleReq){
        return ResponseEntity.ok(userService.grantUserRoles(grantUserRoleReq.getUserId(), grantUserRoleReq.getRole()));
    }
}
