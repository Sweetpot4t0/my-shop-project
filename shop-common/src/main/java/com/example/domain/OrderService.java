package com.example.domain;

import com.example.domain.Member;
import com.example.domain.Order;
import com.example.dto.OrderRequestDto;
import com.example.dto.OrderResponseDto;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정 (성능 최적화)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 1. 주문하기 로직
    @Transactional
    public void placeOrder(OrderRequestDto request, Member loginMember) {
        Order order = Order.builder()
                .member(loginMember)
                .productName(request.getProductName())
                .totalPrice(request.getPrice())
                .quantity(1)
                .orderDate(LocalDateTime.now())
                .build();
        orderRepository.save(order);
    }

    // 2. 컨트롤러 에러를 고치기 위해 추가해야 할 메서드들
    public List<OrderResponseDto> getMyOrders(Long memberId) {
        return orderRepository.findByMemberIdOrderByOrderDateDesc(memberId)
                .stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAllWithMember()
                .stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

        order.cancel(); // 상태 변경 (PAYED -> CANCELED)

        // 재고 복구 로직
        productRepository.findByName(order.getProductName())
                .ifPresent(product -> {
                    // 필드명이 stockQuantity라면 아래처럼!
                    int currentStock = product.getStockQuantity();
                    product.setStockQuantity(currentStock + order.getQuantity());
                });
    }
}