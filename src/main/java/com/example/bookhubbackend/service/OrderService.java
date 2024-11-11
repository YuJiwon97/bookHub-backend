package com.example.bookhubbackend.service;

import com.example.bookhubbackend.dto.CategorySalesStatsDto;
import com.example.bookhubbackend.dto.PopularProductDto;
import com.example.bookhubbackend.dto.SalesStatsDto;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public List<SalesStatsDto> getSalesStats(String period) {
        LocalDateTime startDate = calculateStartDate(period);
        LocalDateTime endDate = LocalDateTime.now();

        System.out.println("시작 날짜: " + startDate);
        System.out.println("종료 날짜: " + endDate);

        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

        System.out.println("주문 개수: " + orders.size());

        Map<String, List<Order>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(order -> formatDate(order.getOrderDate(), period)));

        groupedOrders.forEach((date, orderList) -> {
            System.out.println("날짜: " + date + " -> 주문 수: " + orderList.size());
        });

        return groupedOrders.entrySet().stream()
                .map(entry -> {
                    String date = entry.getKey();
                    List<Order> ordersForDate = entry.getValue();

                    BigDecimal totalSales = ordersForDate.stream()
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int salesCount = ordersForDate.stream()
                            .mapToInt(order -> order.getItems().stream()
                                    .mapToInt(OrderItem::getQuantity)
                                    .sum())
                            .sum();

                    return new SalesStatsDto(date, totalSales, salesCount);
                })
                .sorted(Comparator.comparing(SalesStatsDto::getDate))
                .collect(Collectors.toList());
    }

    public List<CategorySalesStatsDto> getCategorySalesStats(String period) {
        LocalDateTime startDate = calculateStartDate(period);
        LocalDateTime endDate = LocalDateTime.now();
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

        System.out.println("카테고리 통계를 위한 시작 날짜: " + startDate);
        System.out.println("카테고리 통계를 위한 종료 날짜: " + endDate);

        Map<String, BigDecimal> categorySales = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> {
                            Book book = bookRepository.findById(item.getBookId()).orElse(null);
                            return book != null ? book.getCategoryType() : "Unknown";
                        },
                        Collectors.reducing(BigDecimal.ZERO, OrderItem::getTotalPrice, BigDecimal::add)
                ));

        categorySales.forEach((category, totalSales) -> {
            System.out.println("Category: " + category + " -> Total Sales: " + totalSales);
        });

        return categorySales.entrySet().stream()
                .map(entry -> new CategorySalesStatsDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private LocalDateTime calculateStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period.toLowerCase()) {
            case "day" -> now.minusDays(7);
            case "week" -> now.minusWeeks(4);
            case "month" -> now.minusMonths(6);
            case "year" -> now.minusYears(4);
            default -> throw new IllegalArgumentException("유효하지 않은 기간: " + period);
        };
    }

    private String formatDate(LocalDateTime dateTime, String period) {
        DateTimeFormatter formatter = switch (period.toLowerCase()) {
            case "day" -> DateTimeFormatter.ofPattern("yyyy-MM-dd");
            case "week" -> DateTimeFormatter.ofPattern("yyyy-'W'ww");
            case "month" -> DateTimeFormatter.ofPattern("yyyy-MM");
            case "year" -> DateTimeFormatter.ofPattern("yyyy");
            default -> throw new IllegalArgumentException("유효핮지 않은 기간: " + period);
        };
        return dateTime.format(formatter);
    }
}
