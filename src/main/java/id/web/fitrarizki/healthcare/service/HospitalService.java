package id.web.fitrarizki.healthcare.service;

import id.web.fitrarizki.healthcare.dto.HospitalReq;
import id.web.fitrarizki.healthcare.dto.HospitalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalService {
    Page<HospitalResponse> search(String keyword, Pageable pageable);
    HospitalResponse get(Long id);
    HospitalResponse create(HospitalReq hospitalReq);
    HospitalResponse update(Long id, HospitalReq hospitalReq);
    void delete(Long id);
}
