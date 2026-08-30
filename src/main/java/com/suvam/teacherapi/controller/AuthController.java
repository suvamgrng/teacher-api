package com.suvam.teacherapi.controller;

import com.suvam.teacherapi.dto.RegisterRequestDTO;
import com.suvam.teacherapi.dto.RegisterResponseDTO;
import com.suvam.teacherapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponseDTO("Registered successfully", request.username()));
    }
}
