package com.example.repository; // 경로를 com.example.repository로 추천

import com.example.domain.Order; // 이동한 경로에 맞춰 import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // [기본] 마이페이지용 (내가 주문한 것만 보기)
    List<Order> findByMemberIdOrderByOrderDateDesc(Long memberId);

    //  관리자용 (모든 주문 + 주문자 정보를 한 방에 가져오기)

    @Query("SELECT o FROM Order o JOIN FETCH o.member ORDER BY o.orderDate DESC")
    List<Order> findAllWithMember();
}