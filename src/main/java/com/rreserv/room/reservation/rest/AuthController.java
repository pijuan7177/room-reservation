package com.rreserv.room.reservation.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rreserv.room.reservation.rest.model.request.LoginRequest;
import com.rreserv.room.reservation.rest.model.response.LoginResponse;
import com.rreserv.room.reservation.security.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            loginRequest.username(), loginRequest.password()));

            JwtService.IssuedToken issuedToken = jwtService.issue(authentication.getName());

            return ResponseEntity.ok(new LoginResponse(
                    issuedToken.value(), "Bearer", issuedToken.expiresInSeconds()));
        } catch (AuthenticationException authenticationException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
