package com.example;

import com.example.domain.Product;
import com.example.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {
        if (productRepository.count() > 0) return;

        // 데이터를 반복문으로 20개 정도 생성
        String[] names = {"맥북프로", "아이폰15", "에어팟프로", "나이키 러닝화", "갤럭시 S24", "아이패드 에어"};
        for (int i = 1; i <= 20; i++) {
            productRepository.save(Product.builder()
                    .name(names[i % names.length] + " " + i)
                    .price(10000 * i)
                    .stockQuantity(100)
                    .build());
        }
    }

    // 하단에 띄울 가짜 접속자 수 API
    @GetMapping("/active-users")
    public int getActiveUsers() {
        return (int) (Math.random() * 100) + 1; // 1~100명 사이 랜덤
    }



    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /* 특정 상품 상세 조회 */
    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
    }
}