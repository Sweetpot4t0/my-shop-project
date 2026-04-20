package com.example.domain;

import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public void reduceStock(Long id, int qty) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        if (product.getStockQuantity() < qty) {
            throw new RuntimeException("재고가 부족합니다!");
        }

        product.setStockQuantity(product.getStockQuantity() - qty);
    }
}