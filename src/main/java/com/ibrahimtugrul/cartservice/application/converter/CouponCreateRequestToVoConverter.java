package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.CouponCreateRequest;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.vo.CouponCreateVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CouponCreateRequestToVoConverter implements Function<CouponCreateRequest, CouponCreateVo>, Converter<CouponCreateRequest, CouponCreateVo> {
    @Override
    public CouponCreateVo apply(final CouponCreateRequest couponCreateRequest) {
        return CouponCreateVo.builder()
                .discount(Double.valueOf(couponCreateRequest.getDiscount()))
                .discountType(DiscountType.valueOf(couponCreateRequest.getDiscountType().toUpperCase()))
                .minimumAmount(Double.valueOf(couponCreateRequest.getMinimumAmount()))
                .build();
    }

    @Override
    public CouponCreateVo convert(final CouponCreateRequest couponCreateRequest) {
        return apply(couponCreateRequest);
    }
}