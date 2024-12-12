package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;

    /**
     * Crea un ordine per l'utente basandosi sugli elementi nel carrello.
     *
     * @param userId l'ID dell'utente
     * @return l'ordine creato
     */
    @Transactional
    public Order createOrder(Long userId) {
        // Recupera il carrello dell'utente
        Cart cart = cartService.getCartByUserId(userId);

        // Calcola il prezzo totale
        int totalPrice = cart.getCartItems().stream()
                .mapToInt(item -> item.getProduct().getPrice().intValue() * item.getQuantity())
                .sum();

        // Crea l'ordine
        Order order = new Order();
        order.setUser(userService.getUserById(userId));
        order.setProducts((List<CartItem>) cart.getCartItems());
        order.setTotalPrice(totalPrice);
        order.setStatus(Order.Status.PENDING);

        // Associa i cartItems all'ordine
        for (CartItem item : cart.getCartItems()) {
            item.setOrder(order);
        }

        // Salva l'ordine
        orderRepository.save(order);

        // Svuota il carrello
        cartItemService.clearCart(userId);

        return order;
    }

    /**
     * Recupera un ordine per ID.
     *
     * @param orderId l'ID dell'ordine
     * @return l'ordine
     */
    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato con ID: " + orderId));
    }

    /**
     * Recupera tutti gli ordini di un utente.
     *
     * @param user l'ID dell'utente
     * @return la lista di ordini dell'utente
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(User user) {
        return orderRepository.findByUser(user);
    }

    /**
     * Aggiorna lo stato di un ordine.
     *
     * @param orderId l'ID dell'ordine
     * @param status  il nuovo stato
     */
    @Transactional
    public void updateOrderStatus(Long orderId, Order.Status status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }

    /**
     * Cancella un ordine e ripristina gli oggetti nel carrello.
     *
     * @param orderId l'ID dell'ordine
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);

        // Ripristina gli oggetti nel carrello
        for (CartItem item : order.getProducts()) {
            cartItemService.addCartItem(order.getUser().getId(), item.getProduct().getId(), item.getQuantity());
        }

        // Cancella l'ordine
        orderRepository.delete(order);
    }
}


