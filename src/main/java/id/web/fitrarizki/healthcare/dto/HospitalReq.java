package id.web.fitrarizki.healthcare.dto;

import id.web.fitrarizki.healthcare.entity.Hospital;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalReq {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Invalid phone number")
    private String phone;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Description is required")
    private String description;

    public static Hospital toHospital(HospitalReq hospitalReq) {
        return Hospital.builder()
                .name(hospitalReq.getName())
                .address(hospitalReq.getAddress())
                .phone(hospitalReq.getPhone())
                .email(hospitalReq.getEmail())
                .description(hospitalReq.getDescription())
                .build();
    }
}
