package com.example.bookhubbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // Getters and Setters
    private String userId;
    private String password;

}
