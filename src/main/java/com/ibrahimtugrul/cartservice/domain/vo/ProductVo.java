package com.ibrahimtugrul.cartservice.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductVo {
    private Long id;

    private String title;

    private double price;

    private Long categoryId;
}