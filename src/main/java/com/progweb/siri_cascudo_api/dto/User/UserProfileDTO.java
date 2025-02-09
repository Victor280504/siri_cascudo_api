package com.progweb.siri_cascudo_api.dto.User;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private List<String> roles;
}
