package com.ecommerce.model;

import lombok.Data;

import jakarta.persistence.*;


@Data
@MappedSuperclass // Indica che questa è una classe di base che non sarà mappata su una tabella ma le sue proprietà saranno ereditate
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID univoco per ogni entità, generato automaticamente

}
