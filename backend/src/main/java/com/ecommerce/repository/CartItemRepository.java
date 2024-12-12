package com.ecommerce.repository;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    //Cerca per Carrello
    Optional<CartItem> findByCart(Cart cart);
    boolean existsByCart(Cart cart);

    //Cerca per Prodotto
    Optional<CartItem> findByProduct(Product product);
    boolean existsByProduct(Product product);

    //Cerca per Ordine
    Optional<CartItem> findByOrder(Order order);
    Boolean existsByOrder(Order order);

    void deleteByCart(Cart cart);
    void deleteByProduct(Product product);
}
