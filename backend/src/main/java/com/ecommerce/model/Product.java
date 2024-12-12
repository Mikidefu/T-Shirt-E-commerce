package com.ecommerce.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double price;

    @Column
    @Enumerated(EnumType.STRING) // Salva il valore dell'enum come stringa nel database
    private Size size;

    @Column
    private Integer stock;

    @Column
    private String category;

    @Column
    private String ImageUrl;

    @Column
    private Boolean isAvailable;

    // Enum come inner class
    public static enum Size {
        XS, S, M, L, XL, XXL
    }

}
