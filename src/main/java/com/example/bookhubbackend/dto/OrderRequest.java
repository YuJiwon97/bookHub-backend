package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class OrderRequest {
    private String userId;
    private String receiverName;
    private String phoneNumber;
    private Address address;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private int useMileage;
    private int earnMileage;
    private List<OrderItemRequest> items;
    private String orderDate;

    @Setter
    @Getter
    public static class Address {
        private String postalCode;
        private String roadAddress;
        private String detailedAddress;

    }

    // 내부 클래스: 주문 아이템 요청 정보
    @Setter
    @Getter
    public static class OrderItemRequest {
        private Long bookId;
        private int quantity;
        private BigDecimal price;

    }
}
