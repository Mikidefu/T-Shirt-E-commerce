package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;

    /**
     * Ottiene il carrello di un utente.
     *
     * @param userId l'ID dell'utente
     * @return il carrello dell'utente
     */
    @Transactional
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Carrello non trovato per l'utente con ID: " + userId));
    }

    /**
     * Aggiunge un prodotto al carrello di un utente utilizzando il CartItemService.
     *
     * @param userId    l'ID dell'utente
     * @param productId l'ID del prodotto
     * @param quantity  la quantità da aggiungere
     */
    @Transactional
    public void addProductToCart(Long userId, Long productId, int quantity) {
        cartItemService.addCartItem(userId, productId, quantity);
    }

    /**
     * Rimuove un prodotto dal carrello di un utente utilizzando il CartItemService.
     *
     * @param userId    l'ID dell'utente
     * @param productId l'ID del prodotto
     */
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        cartItemService.removeCartItem(userId, productId);
    }
}

