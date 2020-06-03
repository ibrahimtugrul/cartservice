package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Coupon;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponToVoConverterTest {
    private CouponToVoConverter couponToVoConverter;

    @BeforeEach
    public void setup() {
        this.couponToVoConverter = new CouponToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final Coupon coupon = Coupon.builder()
                .minimumAmount(1)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .id(1L)
                .build();

        // when
        final CouponVo couponVo = couponToVoConverter.convert(coupon);

        // then
        assertThat(couponVo).isNotNull();
        assertThat(couponVo.getId()).isEqualTo(coupon.getId());
        assertThat(couponVo.getDiscount()).isEqualTo(coupon.getDiscount());
        assertThat(couponVo.getMinimumAmount()).isEqualTo(coupon.getMinimumAmount());
        assertThat(couponVo.getDiscountType()).isEqualTo(coupon.getDiscountType());
    }
}