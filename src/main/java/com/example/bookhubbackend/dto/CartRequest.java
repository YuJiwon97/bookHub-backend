package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartRequest {
    // Getters and Setters
    private String userId;  // 변경된 userId
    private Long bookId;
    private Integer quantity;

}
