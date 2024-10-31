package com.example.bookhubbackend.model;

import com.example.bookhubbackend.dto.OrderRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    private String receiverName;
    private String phoneNumber;
    private String postalCode;
    private String roadAddress;
    private String detailedAddress;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal shippingFee;

    @Column(nullable = false)
    private int useMileage;

    @Column(nullable = false)
    private int earnMileage;

    private LocalDateTime orderDate;

    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    // 기본 생성자
    public Order() {
        this.status = "결제완료";
    }

    public void updateFromOrderRequest(OrderRequest orderRequest) {
        this.userId = orderRequest.getUserId();
        this.receiverName = orderRequest.getReceiverName();
        this.phoneNumber = orderRequest.getPhoneNumber();
        if (orderRequest.getAddress() != null) {
            this.postalCode = orderRequest.getAddress().getPostalCode();
            this.roadAddress = orderRequest.getAddress().getRoadAddress();
            this.detailedAddress = orderRequest.getAddress().getDetailedAddress();
        }
        this.totalAmount = orderRequest.getTotalAmount();
        this.shippingFee = orderRequest.getShippingFee();
        this.useMileage = orderRequest.getUseMileage();
        this.earnMileage = orderRequest.getEarnMileage();

        if (orderRequest.getOrderDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            this.orderDate = LocalDateTime.parse(orderRequest.getOrderDate(), formatter);
        }

    }

}
