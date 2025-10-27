package id.web.fitrarizki.healthcare.dto;

import id.web.fitrarizki.healthcare.common.constant.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrantUserRoleReq {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Role is required")
    private RoleType role;
}
