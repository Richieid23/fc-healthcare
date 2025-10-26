package id.web.fitrarizki.healthcare.controller.admin;

import id.web.fitrarizki.healthcare.dto.HospitalReq;
import id.web.fitrarizki.healthcare.dto.HospitalResponse;
import id.web.fitrarizki.healthcare.service.HospitalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hospitals")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<HospitalResponse> createHospital(@Valid @RequestBody HospitalReq hospitalReq){
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalService.create(hospitalReq));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponse> updateHospital(@PathVariable(name = "id") Long id, @Valid @RequestBody HospitalReq hospitalReq){
        return ResponseEntity.ok(hospitalService.update(id, hospitalReq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable(name = "id") Long id){
        hospitalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
