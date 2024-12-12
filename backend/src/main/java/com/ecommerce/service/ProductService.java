package com.ecommerce.service;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockService stockService;


    public StockService getStockService() {
        return stockService;
    }
    /**
     * Crea un nuovo prodotto e lo salva nel database.
     */
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product(
                productDTO.getName(),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getSize(),
                productDTO.getStock(),
                productDTO.getCategory(),
                productDTO.getImageUrl(),
                productDTO.getIsAvailable()
        );


        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    /**
     * Ottiene un prodotto per ID.
     */
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con ID: " + productId));
        return mapToDTO(product);
    }

    /**
     * Ottiene tutti i prodotti, eventualmente filtrati da categorie o disponibilità.
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Aggiorna i dettagli di un prodotto esistente.
     */
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con ID: " + productId));

        // Aggiorna i dettagli
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setSize(productDTO.getSize());
        existingProduct.setStock(productDTO.getStock());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setIsAvailable(productDTO.getIsAvailable());

        // Aggiorna lo stock attraverso StockService (aggiungi la logica necessaria)
        stockService.updateStock(existingProduct.getId(), productDTO.getStock());

        Product updatedProduct = productRepository.save(existingProduct);
        return mapToDTO(updatedProduct);
    }

    /**
     * Elimina un prodotto per ID.
     */
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Prodotto non trovato con ID: " + productId);
        }
        productRepository.deleteById(productId);
    }

    /**
     * Ottiene prodotti per categoria.
     */
    public List<ProductDTO> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Ottiene prodotti disponibili.
     */
    public List<ProductDTO> getAvailableProducts() {
        List<Product> products = productRepository.findByIsAvailableTrue();
        return products.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // =======================
    // Metodo di utilità
    // =======================

    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSize(),
                product.getCategory(),
                product.getStock(),
                product.getImageUrl(),
                product.getIsAvailable()
        );
    }
}
