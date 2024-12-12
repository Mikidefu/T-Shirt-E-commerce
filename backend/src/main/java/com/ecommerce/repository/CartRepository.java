package com.ecommerce.repository;

import com.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Trova il carrello per utente
    Optional<Cart> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

    Optional<Cart> findById(Long id);
    boolean existsById(Long id);
}
