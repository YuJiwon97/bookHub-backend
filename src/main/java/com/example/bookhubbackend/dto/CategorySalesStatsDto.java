package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CategorySalesStatsDto {
    private String category;
    private BigDecimal totalSales;

    public CategorySalesStatsDto(String category, BigDecimal totalSales) {
        this.category = category;
        this.totalSales = totalSales;
    }

}