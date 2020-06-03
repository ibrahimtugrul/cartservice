package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Coupon;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CouponToVoConverter implements Function<Coupon, CouponVo>, Converter<Coupon, CouponVo> {
    @Override
    public CouponVo apply(final Coupon coupon) {
        return CouponVo.builder()
                .discount(coupon.getDiscount())
                .discountType(coupon.getDiscountType())
                .minimumAmount(coupon.getMinimumAmount())
                .id(coupon.getId())
                .build();
    }

    @Override
    public CouponVo convert(final Coupon coupon) {
        return apply(coupon);
    }
}