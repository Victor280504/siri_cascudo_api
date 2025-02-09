package com.progweb.siri_cascudo_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "custumer_roles")
public class UserRole {
    @EmbeddedId
    private UserRoleId id; // Chave primária composta

    @ManyToOne
    @MapsId("idUser") // Mapeia o idUser da chave composta
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    // Getters e Setters
    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return id.getRole(); // Acessa o role da chave composta
    }

    public void setRole(String role) {
        this.id.setRole(role); // Define o role na chave composta
    }
}