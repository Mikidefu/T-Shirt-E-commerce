package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;  // riferimento al carrello a cui appartiene l'articolo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // riferimento al prodotto

    @Column(nullable = false)
    private int quantity;  // quantità del prodotto nel carrello

    @Column(nullable = false)
    private double price; //prezzo del prodotto nel carrello

    @ManyToOne
    @JoinColumn(name= "ordine_id")
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem cartItem)) return false;
        if (!super.equals(o)) return false;
        return quantity == cartItem.quantity && Objects.equals(cart, cartItem.cart) && Objects.equals(product, cartItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cart, product, quantity);
    }
}
