package com.example.bookhubbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 주문 아이템의 고유 식별자 (Primary Key)

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private Long bookId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
