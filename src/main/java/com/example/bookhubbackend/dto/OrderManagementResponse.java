package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class OrderManagementResponse {
    private LocalDateTime orderDate;
    private Long orderId;
    private String userId;
    private String title;
    private BigDecimal price;
    private int quantity;
    private String status;

    public OrderManagementResponse(LocalDateTime orderDate, Long orderId, String userId, String title, BigDecimal price, int quantity, String status) {
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.userId = userId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

}
