package com.progweb.siri_cascudo_api.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRoleId implements Serializable {
    private Long idUser; // Corresponde a id_user na tabela
    private String role; // Corresponde a role na tabela

    // Getters e Setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Equals e HashCode (obrigatório para chaves primárias compostas)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(idUser, that.idUser) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, role);
    }
}