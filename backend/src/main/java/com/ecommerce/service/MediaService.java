package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class MediaService {

    private final ProductRepository productRepository;

    @Value("${ecommerce.media.upload-dir}")
    private String uploadDir;

    public MediaService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 1. Carica l'immagine per un prodotto
    public String uploadProductImage(Long productId, MultipartFile file) throws IOException {
        // Trova il prodotto
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // Verifica il file
        validateImageFile(file);

        // Crea il percorso per salvare l'immagine
        String imageFileName = productId + "_" + file.getOriginalFilename();
        Path targetLocation = Path.of(uploadDir, imageFileName);

        // Salva l'immagine
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Aggiorna il prodotto con l'URL dell'immagine
        product.setImageUrl(targetLocation.toString());
        productRepository.save(product);

        return targetLocation.toString(); // Restituisce l'URL dell'immagine salvata
    }

    // 2. Ottieni l'URL dell'immagine di un prodotto
    public String getProductImageUrl(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));
        return product.getImageUrl();
    }

    // 3. Elimina l'immagine di un prodotto
    public void deleteProductImage(Long productId) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Elimina il file fisico
            Path path = Path.of(imageUrl);
            Files.deleteIfExists(path);

            // Rimuovi l'URL dell'immagine dal prodotto
            product.setImageUrl(null);
            productRepository.save(product);
        }
    }

    // 4. Valida che il file sia un'immagine
    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Il file caricato non è un'immagine valida.");
        }
    }
}
