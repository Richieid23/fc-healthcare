package id.web.fitrarizki.healthcare.controller;

import id.web.fitrarizki.healthcare.dto.HospitalResponse;
import id.web.fitrarizki.healthcare.service.HospitalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<Page<HospitalResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(hospitalService.search(keyword, pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> get(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(hospitalService.get(id));
    }
}
