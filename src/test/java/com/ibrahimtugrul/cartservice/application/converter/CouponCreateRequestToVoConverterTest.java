package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.CouponCreateRequest;
import com.ibrahimtugrul.cartservice.domain.vo.CouponCreateVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponCreateRequestToVoConverterTest {
    private CouponCreateRequestToVoConverter couponCreateRequestToVoConverter;

    @BeforeEach
    public void setup() {
        this.couponCreateRequestToVoConverter = new CouponCreateRequestToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discountType("amount")
                .discount("15.0")
                .minimumAmount("2")
                .build();

        // when
        final CouponCreateVo couponCreateVo = couponCreateRequestToVoConverter.convert(couponCreateRequest);

        // then
        assertThat(couponCreateVo).isNotNull();
        assertThat(couponCreateVo.getDiscountType().toString()).isEqualTo(couponCreateRequest.getDiscountType().toUpperCase());
        assertThat(couponCreateVo.getDiscount()).isEqualTo(Double.valueOf(Double.valueOf(couponCreateRequest.getDiscount())));
        assertThat(couponCreateVo.getMinimumAmount()).isEqualTo(Double.valueOf(couponCreateRequest.getMinimumAmount()));
    }
}
