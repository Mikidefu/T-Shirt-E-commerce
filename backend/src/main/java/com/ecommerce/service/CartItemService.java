package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public CartItem addCartItem(Long userId, Long productId, int quantity) {
        // Recupera il carrello dell'utente
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user with id: " + userId));

        // Verifica che il prodotto esista
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        // Verifica che il prodotto sia disponibile in quantità sufficiente
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }

        // Cerca se il prodotto è già nel carrello
        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            // Se il prodotto esiste già nel carrello, aggiorna la quantità
            int newQuantity = existingCartItem.getQuantity() + quantity;
            if (product.getStock() < newQuantity) {
                throw new IllegalArgumentException("Insufficient stock for product after update: " + product.getName());
            }
            existingCartItem.setQuantity(newQuantity);
            cartItemRepository.save(existingCartItem);
        } else {
            // Se il prodotto non è già nel carrello, crea un nuovo CartItem
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setPrice(product.getPrice());
            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem);
        }

        // Aggiorna lo stock del prodotto
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        // Salva il carrello aggiornato
        cartRepository.save(cart);

        // Ritorna il nuovo elemento o quello aggiornato
        return existingCartItem != null ? existingCartItem : cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Failed to add or retrieve CartItem"));
    }


    @Transactional
    public void removeCartItem(Long userId, Long productId) {
        // Recupera il carrello dell'utente
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user with id: " + userId));

        // Cerca il CartItem nel carrello dell'utente
        CartItem cartItemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in the cart for user with id: " + userId));

        // Aumenta lo stock del prodotto rimosso
        Product product = cartItemToRemove.getProduct();
        product.setStock(product.getStock() + cartItemToRemove.getQuantity());
        productRepository.save(product);

        // Rimuovi l'elemento dal carrello e dal repository
        cart.getCartItems().remove(cartItemToRemove);
        cartItemRepository.delete(cartItemToRemove);

        // Salva il carrello aggiornato
        cartRepository.save(cart);
    }


    
    
    public Optional<CartItem> getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    @Transactional
    public void clearCart(Long userId) {
        // Recupera il carrello dell'utente
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrello non trovato per l'utente con ID: " + userId));

        // Rimuove tutti gli articoli nel carrello
        cartItemRepository.deleteByCart(cart);

        // Opzionale: resettare il carrello per assicurarsi che non contenga più oggetti
        cart.getCartItems().clear();  // (Se vuoi anche svuotare la lista in memoria)

        // Salva il carrello vuoto
        cartRepository.save(cart);
    }
}
