package com.example;

import com.example.domain.Member;
import com.example.domain.Order;
import com.example.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;


    @GetMapping("/api/member/orders")
    public List<Order> getMyOrders(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");


        System.out.println("마이페이지 조회 시도 유저: " + loginMember);

        if (loginMember == null) return new ArrayList<>();
        return orderRepository.findByMemberIdOrderByOrderDateDesc(loginMember.getId());
    }

    @GetMapping("/api/admin/orders")
    public List<Order> getAllOrders(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");


        if (loginMember != null) {
            System.out.println("접속 유저: " + loginMember.getName());
            System.out.println("접속 권한: " + loginMember.getRole());
        } else {
            System.out.println("세션에 유저 정보가 없습니다!");
        }

        if (loginMember == null || !"ADMIN".equals(loginMember.getRole())) {
            return new ArrayList<>(); // 권한 없으면 빈 리스트 리턴 -> 화면에서 에러 발생
        }
        return orderRepository.findAllWithMember();
    }
    // 마이페이지 데이터 축적
    @PostMapping("/api/order/buy")
    public String buyProduct(@RequestParam String productName,
                             @RequestParam int price,
                             HttpSession session) {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) return "로그인이 필요합니다.";

        // 1. 주문 객체 생성
        Order order = Order.builder()
                .member(loginMember)
                .productName(productName)
                .totalPrice(price)
                .quantity(1) // 우선 1개로 고정
                .orderDate(LocalDateTime.now())
                .build();

        // 2. DB에 저장 (영수증 발행)
        orderRepository.save(order);

        return "success";
    }
}