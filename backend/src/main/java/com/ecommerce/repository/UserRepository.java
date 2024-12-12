package com.ecommerce.repository;

import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Trova un utente per email
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // Trova un utente per username
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    // Trova un utente per Cart
    Optional<User> findByCart(Cart cart);
    boolean existsByCart(Cart cart);

    // Trova un utente per username e password
    Optional<User> findByUsernameAndPassword(String username, String password);
    boolean existsByUsernameAndPassword(String username, String password);

    // Trova un utente per email e password
    Optional<User> findByEmailAndPassword(String email, String password);
    boolean existsByEmailAndPassword(String email, String password);

    // Trova gli utenti per ruolo
    List<User> findByRole(Role role);
    boolean existsByRole(Role role);

    Optional<User> findByEmailOrUsername(String email, String username);
    boolean existsByEmailOrUsername(String username, String email);
}
