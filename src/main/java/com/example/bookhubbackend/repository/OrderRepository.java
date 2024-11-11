package com.example.bookhubbackend.repository;

import com.example.bookhubbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);
    List<Order> findByUserIdAndOrderDateBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
