package com.example.bookhubbackend.repository;

import com.example.bookhubbackend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserIdAndIsDeletedFalse(String userId);

    long countByUserIdAndIsDeletedFalse(String userId);
}
