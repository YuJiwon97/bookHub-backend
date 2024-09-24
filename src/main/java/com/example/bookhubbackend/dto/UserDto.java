package com.example.bookhubbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor  // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 생성
public class UserDto {
    private Long id;
    private String userName;
    private String userId;
    private String password;
    private String userEmail;
    private String userPhone;
    private Integer mileage;
}
