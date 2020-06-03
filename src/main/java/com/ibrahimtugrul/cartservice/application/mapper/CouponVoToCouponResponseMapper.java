package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CouponResponse;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CouponVoToCouponResponseMapper implements Function<CouponVo, CouponResponse> {
    @Override
    public CouponResponse apply(final CouponVo couponVo) {
        return CouponResponse.builder()
                .discount(String.valueOf(couponVo.getDiscount()))
                .discountType(couponVo.getDiscountType().toString())
                .minimumAmount(String.valueOf(couponVo.getMinimumAmount()))
                .id(String.valueOf(couponVo.getId()))
                .build();
    }
}
