package id.web.fitrarizki.healthcare.service.impl;

import id.web.fitrarizki.healthcare.common.util.DateUtil;
import id.web.fitrarizki.healthcare.config.JwtSecretConfig;
import id.web.fitrarizki.healthcare.dto.UserInfo;
import id.web.fitrarizki.healthcare.service.JwtService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtSecretConfig jwtSecretConfig;
    private final SecretKey signKey;

    @Override
    public String generateToken(UserInfo userInfo) {
        LocalDateTime expirationTime = LocalDateTime.now().plus(jwtSecretConfig.getJwtExpirationTime());
        Date expiry = DateUtil.convertLocalDateTimeToDate(expirationTime);


        return Jwts.builder()
                .subject(userInfo.getUsername())
                .issuedAt(new Date())
                .expiration(expiry)
                .signWith(signKey)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(signKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error while parsing jwt {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(signKey).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
