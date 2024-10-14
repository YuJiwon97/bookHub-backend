package com.example.bookhubbackend.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OrderItemResponse {

    private Long id;
    private Long bookId;
    private String title;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    public OrderItemResponse(Long id, Long bookId, String title, int quantity, BigDecimal price, BigDecimal totalPrice) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }
}