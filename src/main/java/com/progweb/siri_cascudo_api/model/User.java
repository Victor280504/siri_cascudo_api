package com.progweb.siri_cascudo_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Table(name = "customer")
@Entity(name = "customer")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> roles;

}
