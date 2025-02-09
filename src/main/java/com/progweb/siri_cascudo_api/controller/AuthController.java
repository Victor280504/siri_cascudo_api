package com.progweb.siri_cascudo_api.controller;

import com.progweb.siri_cascudo_api.dto.Auth.LoginResponseDTO;
import com.progweb.siri_cascudo_api.dto.Auth.UserLoginDTO;
import com.progweb.siri_cascudo_api.dto.Auth.UserRegisterDTO;
import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint de registro
    @PostMapping("/register")
    public ResponseEntity<CreateResponseDTO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        CreateResponseDTO createResponseDTO = authService.register(userRegisterDTO);
        return new ResponseEntity<CreateResponseDTO>(createResponseDTO, HttpStatus.CREATED);
    }

    // Endpoint de login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        LoginResponseDTO loginResponse = authService.login(userLoginDTO);
        return new ResponseEntity<LoginResponseDTO>(loginResponse, HttpStatus.OK);
    }

    // Endpoint de logout (opcional, depende da implementação)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}