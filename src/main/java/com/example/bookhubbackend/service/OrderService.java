package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.Order;

import com.example.bookhubbackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Integer> getOrderStatusCounts(String userId) {
        Map<String, Integer> statusCounts = new HashMap<>();
        String[] statuses = { "입금대기", "결제완료", "상품준비중", "배송중", "배송완료", "구매확정" };
        for (String status : statuses) {
            statusCounts.put(status, 0);
        }

        orderRepository.findByUserId(userId).forEach(order -> {
            String status = order.getStatus();
            if (statusCounts.containsKey(status)) {
              statusCounts.put(status, statusCounts.get(status) + 1);
            }
        });
        return statusCounts;
    }

    public List<Order> findOrdersByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByUserIdAndOrderDateBetween(userId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
