package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PopularProductDto {
    private Long bookId;
    private String title;
    private int salesCount;
    private BigDecimal totalSales;
    private String categoryType;
    private String imageUrl;

    public PopularProductDto(Long bookId, String title, int salesCount, BigDecimal totalSales, String categoryType, String imageUrl) {
        this.bookId = bookId;
        this.title = title;
        this.salesCount = salesCount;
        this.totalSales = totalSales;
        this.categoryType = categoryType;
        this.imageUrl = imageUrl;
    }

}
