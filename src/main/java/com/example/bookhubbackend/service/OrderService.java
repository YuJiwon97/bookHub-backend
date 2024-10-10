package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.Order;

import com.example.bookhubbackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order);
        updateUserMileage(order);
        cartService.clearCartByUserId(order.getUserId());
    }

    private void updateUserMileage(Order order) {
        int currentMileage = userService.getUserMileage(order.getUserId());
        int newMileage = currentMileage - order.getUseMileage();
        userService.updateUserMileage(order.getUserId(), newMileage);
        newMileage += order.getEarnMileage();
        userService.updateUserMileage(order.getUserId(), newMileage);
    }
}
