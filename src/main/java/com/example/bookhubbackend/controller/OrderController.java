package com.example.bookhubbackend.controller;

import com.example.bookhubbackend.dto.*;
import com.example.bookhubbackend.model.Order;
import com.example.bookhubbackend.model.OrderItem;
import com.example.bookhubbackend.service.BookService;
import com.example.bookhubbackend.service.OrderService;
import com.example.bookhubbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

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

        return ResponseEntity.ok("주문이 완료되었습니다.");
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Integer>> getOrderStatusCounts(@RequestParam String userId) {
        try {
            Map<String, Integer> statusCounts = orderService.getOrderStatusCounts(userId);
            return ResponseEntity.ok(statusCounts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<OrderResponse>> getRecentOrders(@RequestParam String userId) {
        List<Order> orders = orderService.findOrdersByUserIdAndDateRange(userId, LocalDate.now().minusDays(30), LocalDate.now());

        List<OrderResponse> orderResponses = orders.stream().map(order -> {
            List<OrderItemResponse> itemResponses = order.getItems().stream().map(orderItem -> {
                String title = bookService.findBookTitleById(orderItem.getBookId());
                return new OrderItemResponse(orderItem.getId(), orderItem.getBookId(), title, orderItem.getQuantity(), orderItem.getPrice(), orderItem.getTotalPrice());
            }).collect(Collectors.toList());
            return new OrderResponse(order.getId(), order.getOrderDate(), order.getId(), itemResponses, order.getStatus());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(orderResponses);
    }
}
