package com.ibrahimtugrul.cartservice.infrastructure.rest.integration;

import com.ibrahimtugrul.cartservice.application.model.request.CouponCreateRequest;
import com.ibrahimtugrul.cartservice.domain.entity.Coupon;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.domain.repository.CouponRepository;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestCouponControllerIT extends BaseWebIT {

    @Autowired
    private CouponRepository couponRepository;

    private static final String CATEGORY_URL = "/api/v1/coupon";

    @BeforeEach
    public void setup() {// to delete initial values for integration test health
        couponRepository.deleteAll();
    }

    @Test
    public void should_save_coupon() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumAmount("3.0")
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(post(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isOk());

        final List<Coupon> couponList = couponRepository.findAll();

        assertThat(couponList).isNotEmpty();
        assertThat(couponList.size()).isEqualTo(1);

        final Coupon savedCoupon = couponList.get(0);

        assertThat(savedCoupon.getDiscount()).isEqualTo(Double.valueOf(couponCreateRequest.getDiscount()));
        assertThat(savedCoupon.getDiscountType().toString()).isEqualTo(couponCreateRequest.getDiscountType());
        assertThat(savedCoupon.getMinimumAmount()).isEqualTo(Double.valueOf(couponCreateRequest.getMinimumAmount()));

        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(savedCoupon.getId()))));
    }

    @Test
    public void should_return_coupon_list() throws Exception {
        // given
        final Coupon coupon = Coupon.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .build();

        couponRepository.save(coupon);

        // when
        final ResultActions resultActions = mockMvc.perform(get(CATEGORY_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].id", is(String.valueOf(coupon.getId()))));
        resultActions.andExpect(jsonPath("$[0].minimumAmount", is(String.valueOf(coupon.getMinimumAmount()))));
        resultActions.andExpect(jsonPath("$[0].discount", is(String.valueOf(coupon.getDiscount()))));
        resultActions.andExpect(jsonPath("$[0].discountType", is(coupon.getDiscountType().toString())));
    }

    @Test
    public void should_return_coupon_when_coupon_found() throws Exception {
        // given
        final Coupon coupon = Coupon.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .build();

        couponRepository.save(coupon);

        // when
        final ResultActions resultActions = mockMvc.perform(get(StringUtils.join(CATEGORY_URL, "/{couponId}")
                , coupon.getId()));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(coupon.getId()))));
        resultActions.andExpect(jsonPath("$.minimumAmount", is(String.valueOf(coupon.getMinimumAmount()))));
        resultActions.andExpect(jsonPath("$.discount", is(String.valueOf(coupon.getDiscount()))));
        resultActions.andExpect(jsonPath("$.discountType", is(coupon.getDiscountType().toString())));
    }

    @Test
    public void should_delete_coupon_when_coupon_found() throws  Exception {
        final Coupon coupon = Coupon.builder()
                .minimumAmount(3)
                .discountType(DiscountType.RATE)
                .discount(15.0)
                .build();

        couponRepository.save(coupon);

        // when
        final ResultActions resultActions = mockMvc.perform(delete(StringUtils.join(CATEGORY_URL, "/{couponId}")
                , coupon.getId()));

        // then
        resultActions.andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        couponRepository.deleteAll();
    }
}
