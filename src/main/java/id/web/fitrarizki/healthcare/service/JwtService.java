package id.web.fitrarizki.healthcare.service;

import id.web.fitrarizki.healthcare.dto.UserInfo;

public interface JwtService {
    String generateToken(UserInfo userInfo);
    boolean validateToken(String token);
    String extractUsername(String token);
}
