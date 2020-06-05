package com.ibrahimtugrul.cartservice.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CartItemVo {
    private Long productId;
    private Long quantity;
    private Long categoryId;
    private Long appliedCampaign;
    private double totalAmount;
    private double totalAmountAfterCampaign;
}