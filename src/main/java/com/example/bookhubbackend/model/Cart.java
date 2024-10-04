package com.example.bookhubbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "cart")
public class Cart {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId='" + userId + '\'' +
                ", bookId=" + bookId +
                ", quantity=" + quantity +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
