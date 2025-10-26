package id.web.fitrarizki.healthcare.service.impl;

import id.web.fitrarizki.healthcare.common.exeption.ResourceNotFoundException;
import id.web.fitrarizki.healthcare.dto.HospitalReq;
import id.web.fitrarizki.healthcare.dto.HospitalResponse;
import id.web.fitrarizki.healthcare.entity.Hospital;
import id.web.fitrarizki.healthcare.repository.HospitalRepository;
import id.web.fitrarizki.healthcare.service.CacheService;
import id.web.fitrarizki.healthcare.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final CacheService cacheService;

    private static String HOSPITAL_CACHE_KEY = "cache:key:hospital:";

    @Override
    public Page<HospitalResponse> search(String keyword, Pageable pageable) {
        return hospitalRepository.findByNameContainingIgnoreCase(keyword, pageable).map(HospitalResponse::fromHospital);
    }

    @Override
    public HospitalResponse get(Long id) {
        String key = HOSPITAL_CACHE_KEY + id;
        return cacheService.get(key, HospitalResponse.class)
                .orElseGet(() -> hospitalRepository.findById(id)
                        .map(HospitalResponse::fromHospital)
                        .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id ))
                );
    }

    @Override
    public HospitalResponse create(HospitalReq hospitalReq) {
        Hospital hospital = HospitalReq.toHospital(hospitalReq);
        return HospitalResponse.fromHospital(hospitalRepository.save(hospital));
    }

    @Override
    public HospitalResponse update(Long id, HospitalReq hospitalReq) {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id));
        hospital.setName(hospitalReq.getName());
        hospital.setAddress(hospitalReq.getAddress());
        hospital.setPhone(hospitalReq.getPhone());
        hospital.setEmail(hospitalReq.getEmail());
        hospital.setDescription(hospitalReq.getDescription());

        String key = HOSPITAL_CACHE_KEY + id;
        cacheService.evict(key);
        return HospitalResponse.fromHospital(hospitalRepository.save(hospital));
    }

    @Override
    public void delete(Long id) {
        hospitalRepository.delete(hospitalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + id)));
        String key = HOSPITAL_CACHE_KEY + id;
        cacheService.evict(key);
    }
}
