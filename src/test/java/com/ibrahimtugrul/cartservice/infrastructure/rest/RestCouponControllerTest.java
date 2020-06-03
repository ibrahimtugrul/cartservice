package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.manager.CouponManager;
import com.ibrahimtugrul.cartservice.application.model.request.CouponCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CouponResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.enums.DiscountType;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RestCouponControllerTest {
    
    @Mock
    private CouponManager couponManager;

    private MockMvc mockMvc;

    private static final String CAMPAIGN_URL = "/api/v1/coupon";
    private static final String ERR_MISSING_COUPON_MINIMUM_AMOUNT = "coupon.validation.required.minimumAmount";
    private static final String ERR_MISSING_COUPON_DISCOUNT = "coupon.validation.required.discount";
    private static final String ERR_MISSING_COUPON_DISCOUNT_TYPE = "coupon.validation.required.discountType";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RestCouponController(couponManager)).build();
    }

    @Test
    public void should_return_coupon_id_when_coupon_create_request_is_valid() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumAmount("3")
                .build();

        final IdResponse couponResponse = IdResponse.builder()
                .id("1L")
                .build();

        // when
        when(couponManager.create(couponCreateRequest)).thenReturn(couponResponse);

        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is("1L")));
        verify(couponManager, times(1)).create(couponCreateRequest);
    }

    @Test
    public void should_return_bad_request_when_coupon_minimum_amount_is_null() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discount("15.0")
                .discountType("AMOUNT")
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_COUPON_MINIMUM_AMOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_coupon_minimum_amount_is_empty() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumAmount(StringUtils.EMPTY)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_COUPON_MINIMUM_AMOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_coupon_minimum_amount_is_blank() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumAmount(StringUtils.SPACE)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_COUPON_MINIMUM_AMOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_coupon_discount_is_not_null() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discountType("AMOUNT")
                .minimumAmount("1")
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_COUPON_DISCOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_coupon_discount_is_not_empty() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discountType("AMOUNT")
                .minimumAmount("1")
                .discount(StringUtils.EMPTY)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_COUPON_DISCOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_coupon_discount_is_not_blank() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discountType("AMOUNT")
                .minimumAmount("1")
                .discount(StringUtils.SPACE)
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_COUPON_DISCOUNT)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_coupon_discountType_is_not_valid() throws Exception {
        // given
        final CouponCreateRequest couponCreateRequest = CouponCreateRequest.builder()
                .discountType("INVALID")
                .discount("15.0")
                .minimumAmount("1")
                .build();

        // when
        final ResultActions resultActions =  mockMvc.perform(post(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(couponCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_COUPON_DISCOUNT_TYPE)).isTrue();
    }

    @Test
    public void should_return_all_coupons() throws Exception {
        // given
        final CouponResponse couponResponse = CouponResponse.builder()
                .id("1")
                .discount("15.0")
                .discountType("AMOUNT")
                .minimumAmount("3")
                .build();

        final CouponResponse couponResponse1 = CouponResponse.builder()
                .id("2")
                .discount("50.0")
                .discountType("RATE")
                .minimumAmount("5")
                .build();

        // when
        when(couponManager.listAll()).thenReturn(List.of(couponResponse, couponResponse1));

        final ResultActions resultActions =  mockMvc.perform(get(CAMPAIGN_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(2)));
        resultActions.andExpect(jsonPath("$[0].id", is(couponResponse.getId())));
        resultActions.andExpect(jsonPath("$[0].minimumAmount", is(couponResponse.getMinimumAmount())));
        resultActions.andExpect(jsonPath("$[0].discountType", is(couponResponse.getDiscountType())));
        resultActions.andExpect(jsonPath("$[0].discount", is(couponResponse.getDiscount())));
        resultActions.andExpect(jsonPath("$[1].id", is(couponResponse1.getId())));
        resultActions.andExpect(jsonPath("$[1].minimumAmount", is(couponResponse1.getMinimumAmount())));
        resultActions.andExpect(jsonPath("$[1].discountType", is(couponResponse1.getDiscountType())));
        resultActions.andExpect(jsonPath("$[1].discount", is(String.valueOf(couponResponse1.getDiscount()))));
    }

    @Test
    public void should_return_coupon_when_coupon_found_with_id() throws Exception {
        // given
        final Long couponId = 1L;

        final CouponResponse couponResponse = CouponResponse.builder()
                .id("1")
                .discount("50.0")
                .discountType("RATE")
                .minimumAmount("5")
                .build();

        // when
        when(couponManager.retrieveCoupon(couponId)).thenReturn(couponResponse);

        final ResultActions resultActions =  mockMvc.perform(
                get(StringUtils.join(CAMPAIGN_URL, "/{couponId}"), couponId));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(couponResponse.getId())));
        resultActions.andExpect(jsonPath("$.minimumAmount", is(couponResponse.getMinimumAmount())));
        resultActions.andExpect(jsonPath("$.discountType", is(couponResponse.getDiscountType())));
        resultActions.andExpect(jsonPath("$.discount", is(couponResponse.getDiscount())));

    }

    @Test
    public void should_delete_coupon_when_coupon_found_with_id() throws Exception {
        // given
        final Long couponId = 1L;

        // when
        final ResultActions resultActions =  mockMvc.perform(
                delete(StringUtils.join(CAMPAIGN_URL, "/{couponId}"), couponId));

        // then
        resultActions.andExpect(status().isOk());
        verify(couponManager, times(1)).delete(couponId);
    }
}