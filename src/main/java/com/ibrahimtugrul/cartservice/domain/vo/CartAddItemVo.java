package com.ibrahimtugrul.cartservice.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CartAddItemVo {
    private Long productId;
    private Long quantity;
}