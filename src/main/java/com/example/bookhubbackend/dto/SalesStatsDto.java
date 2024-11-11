package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SalesStatsDto {
    private String date;
    private BigDecimal totalSales;
    private int salesCount;

    public SalesStatsDto(String date, BigDecimal totalSales, int salesCount) {
        this.date = date;
        this.totalSales = totalSales;
        this.salesCount = salesCount;
    }

}
