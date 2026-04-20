package com.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter @Setter
@Builder              // 빌더메소드 생성
@NoArgsConstructor    // 빌더 기본 생성자가 필수
@AllArgsConstructor   // 모든 필드를 인자로 받는 생성자
public class Product {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    // 재고 차감 비즈니스 로직
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new RuntimeException("재고가 부족합니다!");
        }
        this.stockQuantity = restStock;
    }
}