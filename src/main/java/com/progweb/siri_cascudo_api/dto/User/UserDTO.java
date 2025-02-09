package com.progweb.siri_cascudo_api.dto.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
}
