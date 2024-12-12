package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long id; // ID del carrello
    private Long userId; // ID dell'utente associato al carrello
    private List<CartItemDTO> items; // Lista degli articoli nel carrello
}
