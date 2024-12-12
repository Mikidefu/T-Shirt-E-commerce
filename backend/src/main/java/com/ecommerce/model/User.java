package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.ecommerce.model.Role;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    private String username;
    private String email;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cartUser", unique = true)
    private Cart cart;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String email, String encodedPassword, Role role) {
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }


}