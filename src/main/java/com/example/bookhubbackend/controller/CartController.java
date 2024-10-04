package com.example.bookhubbackend.controller;

import com.example.bookhubbackend.dto.CartItemResponse;
import com.example.bookhubbackend.dto.CartRequest;
import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.model.Cart;
import com.example.bookhubbackend.model.User;
import com.example.bookhubbackend.service.BookService;
import com.example.bookhubbackend.service.CartService;
import com.example.bookhubbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartRequest cartRequest) {
        try {
            // 요청 데이터의 유효성 검사
            if (cartRequest.getUserId() == null || cartRequest.getBookId() == null || cartRequest.getQuantity() == null || cartRequest.getQuantity() <= 0) {
                return new ResponseEntity<>("잘못된 요청 데이터.", HttpStatus.BAD_REQUEST);
            }

            // userId와 bookId를 사용하여 사용자와 책을 조회
            Optional<User> userOptional = userService.findByUserId(cartRequest.getUserId());
            Optional<Book> bookOptional = bookService.findById(cartRequest.getBookId());

            if (userOptional.isEmpty() || bookOptional.isEmpty()) {
                return new ResponseEntity<>("사용자 또는 책을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
            }

            // CartService를 이용해 장바구니에 책 추가.
            cartService.addToCart(userOptional.get(), bookOptional.get(), cartRequest.getQuantity());

            // 성공적으로 장바구니에 추가되었을 때 200 응답 반환.
            return new ResponseEntity<>("책이 장바구니에 추가.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("장바구니에 책을 추가하는 중 오류 발생", e);
            return new ResponseEntity<>("서버에서 장바구니에 책을 추가하는 중 오류 발생.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCartItemsByUser(@PathVariable("userId") String userId) {
        try {
            Optional<User> userOptional = userService.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Cart> cartItems = cartService.getCartItemsByUser(userOptional.get());

            List<CartItemResponse> response = cartItems.stream()
                    .map(cartItem -> {
                        Optional<Book> bookOptional = bookService.findById(cartItem.getBookId());
                        Book book = bookOptional.orElse(null);
                        return new CartItemResponse(cartItem, book);
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateCartItemQuantity(@PathVariable("id") Long id, @RequestBody CartRequest cartRequest) {
        try {
            if (id == null || cartRequest.getQuantity() == null || cartRequest.getQuantity() <= 0) {
                return new ResponseEntity<>("유효하지 않은 요청 데이터", HttpStatus.BAD_REQUEST);
            }

            Optional<Cart> cartOptional = cartService.findById(id);
            if (cartOptional.isEmpty()) {
                return new ResponseEntity<>("장바구니 항목을 찾을 수 없습니다", HttpStatus.NOT_FOUND);
            }

            Cart cartItem = cartOptional.get();
            cartItem.setQuantity(cartRequest.getQuantity());
            cartService.save(cartItem);

            return new ResponseEntity<>("장바구니 항목 수량이 업데이트되었습니다", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("장바구니 항목 수량 업데이트 중 오류 발생", e);
            return new ResponseEntity<>("장바구니 항목 수량 업데이트 중 서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/remove/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        if (id == null) {
            logger.error("장바구니 ID는 null일 수 없습니다");
            return ResponseEntity.badRequest().build();
        }

        try {
            Optional<Cart> cartOptional = cartService.findById(id);
            if (cartOptional.isEmpty()) {
                logger.warn("ID {}를 가진 장바구니 항목을 찾을 수 없습니다", id);
                return ResponseEntity.notFound().build();
            }

            cartService.removeFromCart(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("장바구니 항목 제거 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
