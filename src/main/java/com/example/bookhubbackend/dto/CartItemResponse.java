package com.example.bookhubbackend.dto;

import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.model.Cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemResponse {

    // Getters and Setters
    @JsonProperty("cartId")
    private Long cartId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("bookId")
    private Long bookId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("book")
    private Book book;

    public CartItemResponse(Cart cart, Book book) {
        this.cartId = cart.getCartId();
        this.userId = cart.getUserId();
        this.bookId = cart.getBookId();
        this.quantity = cart.getQuantity();
        this.book = book;
    }

}
