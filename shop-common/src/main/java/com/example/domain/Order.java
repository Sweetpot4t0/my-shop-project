package com.example.domain; // com.example.controller.domain -> com.example.domain으로 변경 권장

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "orders")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    private String productName;
    private int quantity;
    private int totalPrice;
    private LocalDateTime orderDate;
    // Order 엔티티에 상태 필드 추가
    private String status = "PAYED"; // 기본값: 결제완료

    public void cancel() {
        this.status = "CANCELED";
    }
}