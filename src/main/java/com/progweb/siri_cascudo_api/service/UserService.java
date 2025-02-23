package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.dto.User.UserDTO;
import com.progweb.siri_cascudo_api.dto.User.UserProfileDTO;
import com.progweb.siri_cascudo_api.dto.User.UserUpdateDTO;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.User;
import com.progweb.siri_cascudo_api.model.UserRole;
import com.progweb.siri_cascudo_api.repository.UserRepository;
import com.progweb.siri_cascudo_api.repository.UserRoleRepository;
import com.progweb.siri_cascudo_api.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return mapToUserDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public UserProfileDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<UserRole> roles = userRoleRepository.findByUser_Id(user.getId());

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(user.getId());
        userProfileDTO.setName(user.getName());
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setAddress(user.getAddress());
        userProfileDTO.setWallet(user.getWallet());
        userProfileDTO.setRoles(roles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList()));

        return userProfileDTO;
    }

    public UpdateResponseDTO<UserDTO> updateUser(String email, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (userUpdateDTO.getName() != null && !userUpdateDTO.getName().isEmpty()) {
            user.setName(userUpdateDTO.getName());
        }
        if (userUpdateDTO.getAddress() != null && !userUpdateDTO.getAddress().isEmpty()) {
            user.setAddress(userUpdateDTO.getAddress());
        }

        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().isEmpty()) {
            user.setEmail(userUpdateDTO.getEmail());
        }

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.encryptPassword(userUpdateDTO.getPassword())); // Criptografa a nova senha
        }

        User updatedUser = userRepository.save(user);

        return new UpdateResponseDTO<>(updatedUser.getId().toString(), "Usuário atualizado com sucesso.", mapToUserDTO(updatedUser));
    }

    public CreateResponseDTO deleteUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String userId = user.getId().toString();
        userRepository.delete(user);

        return new CreateResponseDTO(userId, "Usuário deletado com sucesso.");
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setWallet(user.getWallet());
        return userDTO;
    }
}