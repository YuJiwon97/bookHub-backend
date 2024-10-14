package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderResponse {

    private Long orderId;
    private LocalDateTime orderDate;
    private Long orderNumber;
    private List<OrderItemResponse> items;
    private String status;

    public OrderResponse(Long orderId, LocalDateTime orderDate, Long orderNumber, List<OrderItemResponse> items, String status) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderNumber = orderNumber;
        this.items = items;
        this.status = status;
    }

}

