package com.example.bookhubbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Book {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String translator;
    private String publisher;
    private Double originalPrice;
    private Double sellingPrice;
    private Double shippingFee;
    private String category;
    private String categoryType;
    private String imageUrl;
    private String description;
    private String estimatedArrival;
}
