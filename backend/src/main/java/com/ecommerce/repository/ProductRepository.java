package com.ecommerce.repository;

import com.ecommerce.model.Product;
import com.ecommerce.model.Product.Size; // Importa l'enum Size
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Trova tutti i prodotti per nome
    List<Product> findByName(String name);
    boolean existsByName(String name);

    // Trova i prodotti per categoria (se aggiungi una proprietà 'category' a Product)
    List<Product> findByCategory(String category);
    boolean existsByCategory(String category);

    // Trova tutti i prodotti con stock maggiore di una certa quantità
    List<Product> findByStockGreaterThan(Integer stock);
    boolean existsByStock(Integer stock);

    // Trova i prodotti per taglia
    List<Product> findBySize(Size size);
    boolean existsBySize(Size size);

    // Trova i prodotti per prezzo minore uguale
    List<Product> findByPriceLessThan(Double price);
    boolean existsByPriceLessThan(Double price);

    List<Product> findByIsAvailableTrue();
}
