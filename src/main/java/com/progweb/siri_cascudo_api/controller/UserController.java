package com.progweb.siri_cascudo_api.controller;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.dto.User.UserProfileDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.progweb.siri_cascudo_api.dto.User.UserDTO;
import com.progweb.siri_cascudo_api.dto.User.UserUpdateDTO;
import com.progweb.siri_cascudo_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // O email é o "username" no contexto de segurança
        // Busca o perfil do usuário pelo email
        UserProfileDTO userProfile = userService.getProfile(email);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UpdateResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // O email é o "username" no contexto de segurança
        UpdateResponseDTO userDTO = userService.updateUser(email, userUpdateDTO);
        return new ResponseEntity<UpdateResponseDTO>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<CreateResponseDTO> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        CreateResponseDTO deletedUser = userService.deleteUser(email);
        return new ResponseEntity<CreateResponseDTO>(deletedUser, HttpStatus.NO_CONTENT);
    }
}
