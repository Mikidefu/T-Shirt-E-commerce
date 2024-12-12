package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BuyService {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    //verifica disponibilità dello stock
    public boolean buyable(Cart cart){
        List<CartItem> cartItems = (List<CartItem>) cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            Product productDB = cartItemRepository.findById(cartItem.getId()).get().getProduct();
            Integer quantityDB = productDB.getStock();
            Integer quantityJSON = cartItem.getQuantity();
            if(quantityDB < quantityJSON){
                return false;
            }
        }
        return true;
    }

    @Transactional
    public Order buy(Long userId) {
        // Recupera il carrello dell'utente
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrello non trovato per l'utente con ID: " + userId));

        // Crea un ordine basato sugli articoli nel carrello
        List<CartItem> cartItems = (List<CartItem>) cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Il carrello è vuoto");
        }

        for (CartItem cartItem : cartItems) {
            CartItem cartItemDB = cartItemRepository.findById(cartItem.getId()).get();
            BigDecimal prezzoDB = BigDecimal.valueOf(cartItemDB.getPrice());
            BigDecimal prezzoJSON = BigDecimal.valueOf(cartItem.getPrice());
            if (prezzoDB.compareTo(prezzoJSON) < 0) {
                throw new IllegalArgumentException("Errore: Prezzi non corrispondenti");
            }
        }

        // Calcola il totale dell'ordine
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        //verifica l'integrità dello lo stock
        if(buyable(cart)){
            for (CartItem cartItem : cartItems) {
                Product productDB = cartItemRepository.findById(cartItem.getId()).get().getProduct();
                Long prodID = productDB.getId();
                productService.getStockService().updateStock(prodID,productDB.getStock()-cartItem.getQuantity());

            }
        }else{
            throw new IllegalStateException("Errore: Quantità non corrispondenti");
        }

        // Crea e salva l'ordine
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setProducts(cartItems);
        order.setTotalPrice(totalPrice);
        order.setStatus(Order.Status.PENDING);

        // Salva l'ordine nel database
        Order savedOrder = orderRepository.save(order);

        // Aggiorna lo stock dei prodotti e rimuovi gli articoli dal carrello
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int updatedStock = product.getStock() - cartItem.getQuantity();

            product.setStock(updatedStock); // Aggiorna lo stock del prodotto
            productRepository.save(product); // Salva il prodotto aggiornato

        }

        // Rimuovo gli articoli dal carrello
        cartItemService.clearCart(userId);

        return savedOrder; // Restituisce l'ordine creato
    }
}
