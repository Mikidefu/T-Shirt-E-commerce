package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    // Getter e setter per cartItems
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new HashSet<>();

    // Getter e setter per user
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart cart)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(user, cart.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}