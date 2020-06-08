package com.ibrahimtugrul.cartservice.domain.vo;

import lombok.*;

import java.util.List;

@Value
@Builder
public class CartVo {
    private Long id;
    private List<CartItemVo> items;
    private Long appliedCoupon;
    private double totalAmount;
    private double totalAmountAfterDiscount;
    private double totalAmountAfterCoupon;
    private double couponAmount;
    private double deliveryCost;
}