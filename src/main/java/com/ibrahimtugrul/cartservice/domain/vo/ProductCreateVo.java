package com.ibrahimtugrul.cartservice.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductCreateVo {
    private String title;
    private double price;
    private long categoryId;
}