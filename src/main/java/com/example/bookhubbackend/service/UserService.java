package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.User;
import java.util.Optional;

public interface UserService {

    // 사용자 추가
    User saveUser(User user);

    // 사용자 ID로 조회
    Optional<User> findByUserId(String userId);

}