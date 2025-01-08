package com.example.bookhubbackend.repository;

import com.example.bookhubbackend.model.Order;
import com.example.bookhubbackend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);
    List<Order> findByUserIdAndOrderDateBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 추가: 특정 주문 항목 조회
    @Query("SELECT oi FROM OrderItem oi WHERE oi.id = :orderItemId")
    Optional<OrderItem> findOrderItemById(Long orderItemId);
}
