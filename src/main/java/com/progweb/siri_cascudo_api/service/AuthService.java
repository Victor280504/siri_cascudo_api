package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.Auth.LoginResponseDTO;
import com.progweb.siri_cascudo_api.dto.Auth.UserLoginDTO;
import com.progweb.siri_cascudo_api.dto.Auth.UserRegisterDTO;
import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.exception.CustomException;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.User;
import com.progweb.siri_cascudo_api.model.UserRole;
import com.progweb.siri_cascudo_api.model.UserRoleId;
import com.progweb.siri_cascudo_api.repository.UserRepository;
import com.progweb.siri_cascudo_api.repository.UserRoleRepository;
import com.progweb.siri_cascudo_api.util.JwtUtil;
import com.progweb.siri_cascudo_api.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public CreateResponseDTO register(UserRegisterDTO userRegisterDTO) {
        // Verifica se o email já está em uso
        Optional<User> existingUser = userRepository.findByEmail(userRegisterDTO.getEmail());

        if (existingUser.isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT.value(), "Email indisponível", "Por favor, escolha um endereço de email diferente.");
        }
        // Cria um novo usuário
        User user = new User();
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(PasswordUtil.encryptPassword(userRegisterDTO.getPassword())); // Criptografa a senha
        user.setAddress(userRegisterDTO.getAddress());
        user.setWallet(100.0);

        // Salva o usuário no banco de dados
        User savedUser = userRepository.save(user);

        // Cria a role do usuário
        UserRoleId userId = new UserRoleId();
        userId.setIdUser(savedUser.getId());
        userId.setRole("USER");

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setId(userId);
        userRole.setRole("USER");

        // Salva a role
        UserRole savedRole = userRoleRepository.save(userRole);

        // Verifica se o usuário foi salvo com sucesso
        if (savedUser == null || savedRole == null) {
            // Realiza rollback manual se necessário
            userRepository.deleteById(savedUser.getId());
            userRoleRepository.deleteById(savedRole.getId());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro ao registrar usuário", "Ocorreu um problema inesperado. Tente novamente mais tarde.");
        }

        // Retorna resposta contendo o ID do novo usuário
        return new CreateResponseDTO(savedUser.getId().toString(), "Usuário Registrado com sucesso.");
    }

    public LoginResponseDTO login(UserLoginDTO userLoginDTO) {
        // Busca o usuário pelo email
        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", userLoginDTO.getEmail()));
        // Verifica se a senha está correta
        if (!PasswordUtil.checkPassword(userLoginDTO.getPassword(), user.getPassword())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED.value(), "Email ou senha incorretos", "Tente novamente ou volte mais tarde.");
        }

        // Busca os papéis do usuário
        List<UserRole> roles = userRoleRepository.findByUser_Id(user.getId());

        // Gera um token JWT com os papéis do usuário
        String token = jwtUtil.generateToken(user.getEmail(), roles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList()), user.getId());

        // Retorna a resposta de login com o ID do usuário e o token
        return new LoginResponseDTO(user.getId().toString(), token);
    }

    public void logout(String token) {
        System.out.println("Token invalidated: " + token);
    }

}