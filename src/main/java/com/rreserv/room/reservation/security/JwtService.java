package com.rreserv.room.reservation.security;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String ISSUER = "room-reservation";

    private final JwtEncoder jwtEncoder;

    private final Duration expiration;

    public JwtService(JwtEncoder jwtEncoder,
            @Value("${app.jwt.expiration:PT1H}") Duration expiration) {
        this.jwtEncoder = jwtEncoder;
        this.expiration = expiration;
    }

    public IssuedToken issue(String subject) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(expiration);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new IssuedToken(token, expiration.toSeconds());
    }

    public record IssuedToken(String value, long expiresInSeconds) {
    }
}
