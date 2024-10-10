package com.example.bookhubbackend.controller;

import com.example.bookhubbackend.dto.*;
import com.example.bookhubbackend.model.Order;
import com.example.bookhubbackend.model.OrderItem;
import com.example.bookhubbackend.service.OrderService;
import com.example.bookhubbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/complete")
    public ResponseEntity<String> complete(@RequestBody OrderRequest orderRequest) {
        Order order = new Order();
        order.updateFromOrderRequest(orderRequest);

        List<OrderItem> orderItems = orderRequest.getItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBookId(item.getBookId());
                    orderItem.setPrice(item.getPrice());
                    orderItem.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);
        orderService.createOrder(order);
        userService.applyMileage(orderRequest.getUserId(), orderRequest.getUseMileage(), orderRequest.getEarnMileage());

        return ResponseEntity.ok("Order completed");
    }
}
