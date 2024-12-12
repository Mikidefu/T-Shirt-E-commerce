package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="user")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItem> products;

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public enum Status {
        PENDING,     // Ordine in attesa di conferma
        PROCESSING,  // Ordine in lavorazione
        SHIPPED,     // Ordine spedito
        DELIVERED,   // Ordine consegnato
        CANCELLED,   // Ordine annullato
        RETURNED     // Ordine restituito
    }

}
