package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long id; // ID del CartItem
    private Long cartId; // ID del carrello a cui appartiene
    private Long productId; // ID del prodotto
    private Integer quantity; // Quantità di prodotto nel carrello
    private Double price; // Prezzo del prodotto
    private Long orderId;// ID dell'ordine

}
