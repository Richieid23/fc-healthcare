package id.web.fitrarizki.healthcare.dto;

import id.web.fitrarizki.healthcare.common.constant.RoleType;
import id.web.fitrarizki.healthcare.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRes {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private List<RoleType> roles;

    public static AuthRes fromUserInfoAndToken(UserInfo user, String token) {
        return AuthRes.builder()
                .token(token)
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .build();
    }
}
