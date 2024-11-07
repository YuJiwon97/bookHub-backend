package com.example.bookhubbackend.service;

import com.example.bookhubbackend.dto.PopularProductDto;
import com.example.bookhubbackend.model.Book;
import com.example.bookhubbackend.model.Order;

import com.example.bookhubbackend.model.OrderItem;
import com.example.bookhubbackend.repository.BookRepository;
import com.example.bookhubbackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;
    @Autowired
    private BookRepository bookRepository;


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

    public Map<String, List<PopularProductDto>> getPopularProductsByCategory() {
        List<Order> orders = orderRepository.findAll();
        Map<Long, PopularProductDto> productSalesMap = new HashMap<>();
        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                Book book = bookRepository.findById(item.getBookId()).orElse(null);
                if (book == null) continue;
                PopularProductDto productDTO = productSalesMap.getOrDefault(item.getBookId(),
                        new PopularProductDto(
                                book.getId(),
                                book.getTitle(),
                                0,
                                BigDecimal.ZERO,
                                book.getCategoryType(),
                                book.getImageUrl()
                        ));
                productDTO.setSalesCount(productDTO.getSalesCount() + item.getQuantity());
                productDTO.setTotalSales(productDTO.getTotalSales().add(item.getTotalPrice()));
                productSalesMap.put(item.getBookId(), productDTO);
            }
        }
        return productSalesMap.values().stream()
                .collect(Collectors.groupingBy(
                        PopularProductDto::getCategoryType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparingInt(PopularProductDto::getSalesCount).reversed())
                                        .collect(Collectors.toList())
                        )
                ));
    }
}
