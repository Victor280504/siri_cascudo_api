package com.progweb.siri_cascudo_api.repository;

import com.progweb.siri_cascudo_api.model.UserRole;
import com.progweb.siri_cascudo_api.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUser_Id(Long userId); // Busca os papéis de um usuário pelo ID
}