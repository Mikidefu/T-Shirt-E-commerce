package com.ecommerce.repository;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    boolean existsById(Long id);
    boolean existsByUser(User user);

    Optional<Order> findById(Long id);
    List<Order> findByUser(User user);

}
