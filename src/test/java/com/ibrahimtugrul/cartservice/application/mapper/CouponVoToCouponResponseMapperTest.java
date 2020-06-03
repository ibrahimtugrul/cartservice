package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CouponResponse;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.vo.CouponVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponVoToCouponResponseMapperTest {

    private CouponVoToCouponResponseMapper couponVoToCouponResponseMapper;

    @BeforeEach
    public void setup() {
        this.couponVoToCouponResponseMapper = new CouponVoToCouponResponseMapper();
    }

    @Test
    public void should_map() {
        // given
        final CouponVo couponVo = CouponVo.builder()
                .discount(15.0)
                .discountType(DiscountType.RATE)
                .id(1L)
                .minimumAmount(3)
                .build();

        // when
        final CouponResponse couponResponse = couponVoToCouponResponseMapper.apply(couponVo);

        // then
        assertThat(couponResponse).isNotNull();
        assertThat(couponResponse.getDiscountType()).isEqualTo(couponVo.getDiscountType().toString());
        assertThat(couponResponse.getDiscount()).isEqualTo(String.valueOf(couponVo.getDiscount()));
        assertThat(couponResponse.getMinimumAmount()).isEqualTo(String.valueOf(couponVo.getMinimumAmount()));
        assertThat(couponResponse.getId()).isEqualTo(String.valueOf(couponVo.getId()));
    }
}