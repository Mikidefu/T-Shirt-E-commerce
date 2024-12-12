package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StockService {

    @Autowired
    private ProductRepository productRepository;

    // Verifica la disponibilità di un prodotto
    public boolean isProductAvailable(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con ID: " + productId));
        return product.getStock() > 0 && product.getIsAvailable(); // Verifica che il prodotto sia disponibile
    }

    // Aggiorna la quantità di stock per un prodotto (aggiungendo o sottraendo)
    @Transactional
    public void updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con ID: " + productId));

        // Verifica che lo stock non scenda sotto zero
        if (product.getStock() + quantity < 0) {
            throw new IllegalArgumentException("Non ci sono abbastanza scorte disponibili.");
        }

        // Aggiorna lo stock del prodotto
        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }

    // Restituisce la quantità di stock disponibile per un prodotto
    public int getProductStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con ID: " + productId));
        return product.getStock();
    }

    // Imposta la disponibilità di un prodotto (disponibile o non disponibile)
    @Transactional
    public void setProductAvailability(Long productId, boolean isAvailable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con ID: " + productId));
        product.setIsAvailable(isAvailable);
        productRepository.save(product);
    }
}
