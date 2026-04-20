package com.example;

import com.example.domain.Member;
import com.example.domain.Order;
import com.example.domain.Product;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.domain.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final ProductService productService;
    private final OrderRepository orderRepository; // 주문 기록용
    private final ProductRepository productRepository; // 상품 정보 조회용

    /**
     * 카드 팝업창에서 [결제 승인]을 눌렀을 때 호출되는 API
     */
    @GetMapping("/api/pay/success")
    public String success(@RequestParam("id") Long id,
                          @RequestParam("qty") int qty,
                          HttpSession session) {

        // 1. 세션에서 로그인한 유저 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");

        // 2. 상품 정보 가져오기
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        //
        productService.reduceStock(id, qty);

        // 3. DB 기록 남기기
        if (loginMember != null) {
            Order order = Order.builder()
                    .member(loginMember)
                    .productName(product.getName())
                    .quantity(qty)
                    .totalPrice(product.getPrice() * qty)
                    .orderDate(LocalDateTime.now())
                    .build();

            orderRepository.save(order);
        }

        return "ok";
    }
}