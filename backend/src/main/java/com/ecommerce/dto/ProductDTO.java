package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.ecommerce.model.Product.Size; // Import dell'enum Size dalla classe Product

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Size size; // Campo definito come Enum
    private String category; // Nuovo campo aggiunto
    private Integer stock;
    private String ImageUrl;
    private Boolean isAvailable;
    
}
