package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequestDto {

    @NotBlank(message = "상품명은 필수입니다.")
    private String productName;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 100, message = "최소 주문 금액은 100원 이상입니다.")
    private Integer price;
}