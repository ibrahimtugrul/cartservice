package com.ibrahimtugrul.cartservice.domain.vo;

import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import lombok.*;

@Value
@Builder
public class CouponVo {
    private Long id;
    private double minimumAmount;
    private double discount;
    private DiscountType discountType;
}