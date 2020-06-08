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

    public double calculateDiscountAmount(final double amount) {
        switch (this.discountType) {
            case RATE:
                return (amount * this.discount) / 100;
            case AMOUNT:
                return (amount - this.discount) > 0 ? this.discount : 0.0;
            default:
                return 0.0;
        }
    }
}