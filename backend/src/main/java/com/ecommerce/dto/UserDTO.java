package com.ecommerce.dto;

import lombok.Data;
import com.ecommerce.model.Role;

// UserDTO
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Long cartId;
    private Role role;
}
