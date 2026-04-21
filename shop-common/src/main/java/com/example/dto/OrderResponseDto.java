package com.example.dto;

import com.example.domain.Order;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {
    private Long orderId;
    private String memberName;
    private String loginId;    // 관리자 페이지를 위해 추가
    private String productName;
    private Integer orderPrice;
    private Integer quantity;   // 수량도 추가하면 더 정확하겠죠?
    private LocalDateTime orderDate;
    private String status;

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();

        // 1. 회원 정보 추출 (연관관계)
        if (order.getMember() != null) {
            this.memberName = order.getMember().getName();
            this.loginId = order.getMember().getLoginId();
        }

        // 2. 주문 정보 추출 (Order 엔티티 필드 직접 접근)
        // 에러 원인: order.getProduct().getName() -> order.getProductName()으로 수정
        this.productName = order.getProductName();
        this.orderPrice = order.getTotalPrice(); // totalPrice 필드명 확인
        this.quantity = order.getQuantity();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus(); // 이 줄이 있어야 프론트에서 구분이 가능합니다.
    }

}