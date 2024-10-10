package com.example.bookhubbackend.repository;

import com.example.bookhubbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
}
