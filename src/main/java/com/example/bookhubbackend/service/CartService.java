package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.model.Cart;
import com.example.bookhubbackend.model.User;
import com.example.bookhubbackend.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void addToCart(User user, Book book, Integer quantity) {
        Cart cart = new Cart();
        cart.setUserId(user.getUserId());
        cart.setBookId(book.getId());
        cart.setQuantity(quantity);
        cart.setIsDeleted(false);
        cartRepository.save(cart);
    }

    public List<Cart> getCartItemsByUser(User user) {
        return cartRepository.findByUserIdAndIsDeletedFalse(user.getUserId()); // userId를 String으로
    }

    public Optional<Cart> findById(Long cartId) {
        return cartRepository.findById(cartId);
    }

    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public void removeFromCart(Long cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.setIsDeleted(true);
            cartRepository.save(cart);
        }
    }

    // 장바구니 비우기 메서드 추가
    public void clearCartByUserId(String userId) {
        List<Cart> cartItems = cartRepository.findByUserIdAndIsDeletedFalse(userId);
        for (Cart cart : cartItems) {
            cart.setIsDeleted(true);
        }
        cartRepository.saveAll(cartItems);
    }
}
