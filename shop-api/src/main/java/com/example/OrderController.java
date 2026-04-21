package com.example;

import com.example.domain.Member;
import com.example.domain.Order;
import com.example.domain.OrderService;
import com.example.dto.OrderRequestDto;
import com.example.dto.OrderResponseDto; // DTO 임포트
import com.example.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/api/member/orders")
    public List<OrderResponseDto> getMyOrders(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        return orderService.getMyOrders(loginMember.getId());
    }

    @GetMapping("/api/admin/orders")
    public List<OrderResponseDto> getAllOrders(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (!"ADMIN".equals(loginMember.getRole())) {
            return new ArrayList<>();
        }

        // 에러 지점 수정: 리포지토리가 아니라 서비스를 부릅니다.
        return orderService.getAllOrders();
    }

    @PostMapping("/api/order/buy")
    public String buyProduct(@Valid @RequestBody OrderRequestDto request, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) return "로그인이 필요합니다.";

        // 로직 수정: 직접 빌더 안 쓰고 서비스의 주문 함수 호출
        orderService.placeOrder(request, loginMember);
        return "success";
    }

    @PostMapping("/api/admin/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return "success";
    }
}