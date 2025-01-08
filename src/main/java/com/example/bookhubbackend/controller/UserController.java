package com.example.bookhubbackend.controller;


import com.example.bookhubbackend.config.JwtTokenUtil;

import com.example.bookhubbackend.dto.CheckIdResponse;
import com.example.bookhubbackend.dto.LoginRequest;
import com.example.bookhubbackend.dto.LoginResponse;
import com.example.bookhubbackend.exception.InvalidCredentialsException;
import com.example.bookhubbackend.exception.UserAlreadyExistsException;
import com.example.bookhubbackend.exception.UserNotFoundException;
import com.example.bookhubbackend.service.UserService;
import com.example.bookhubbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    // 회원가입 로직
    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // 사용자 중복 체크
        Optional<User> existingUser = userService.findByUserId(user.getUserId());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("해당 ID " + user.getUserId() + "는 이미 존재하는 ID 입니다.");
        }

        User createrUser = userService.saveUser(user);
        return new ResponseEntity<>(createrUser, HttpStatus.CREATED);
    }

    // 사용자 아이디 중복 체크
    @GetMapping("/check-id")
    public ResponseEntity<CheckIdResponse> checkUserId(@RequestParam String userId) {
        Optional<User> existingUser = userService.findByUserId(userId);
        return existingUser.isPresent()
                ? new ResponseEntity<>(new CheckIdResponse(true), HttpStatus.OK)
                : new ResponseEntity<>(new CheckIdResponse(false), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();

        // 사용자 확인
        User user = userService.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 및 반환
        String token = JwtTokenUtil.generateToken(user.getUserId());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        Optional<User> user = userService.findByUserId(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 사용자 조회 (ID로)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
