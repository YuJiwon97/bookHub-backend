package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.User;
import java.util.Optional;

public interface UserService {

    // 사용자 추가
    User saveUser(User user);

    // 사용자 ID로 조회
    Optional<User> findByUserId(String userId);

    // 마일리지 사용 및 적립 적용
    void applyMileage(String userId, int useMileage, int earnMileage);

    // 마일리지 조회 메서드
    int getUserMileage(String userId);

    // 마일리지 업데이트 메서드
    void updateUserMileage(String userId, int mileageChange);
}