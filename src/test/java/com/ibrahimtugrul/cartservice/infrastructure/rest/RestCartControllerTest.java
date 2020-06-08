package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.manager.CartManager;
import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CartItemResponse;
import com.ibrahimtugrul.cartservice.application.model.response.CartResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.infrastructure.rest.util.WebTestUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
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
public class RestCartControllerTest {

    @Mock
    private CartManager cartManager;

    private MockMvc mockMvc;

    private static final String CART_URL = "/api/v1/cart";
    private static final String ERR_MISSING_PRODUCT_ID = "cart.validation.required.productId";
    private static final String ERR_MISSING_QUANTITY = "cart.validation.required.quantity";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RestCartController(cartManager)).build();
    }

    @Test
    public void should_create_cart() throws Exception {
        // given
        final String cartId = "id";

        // when
        when(cartManager.create()).thenReturn(IdResponse.builder().id(cartId).build());

        final ResultActions resultActions = mockMvc.perform(post(CART_URL));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is("id")));
        verify(cartManager, times(1)).create();
    }

    @Test
    public void should_list_all_carts() throws Exception {
        // given
        final CartItemResponse cartItemResponse = CartItemResponse.builder()
                .appliedCampaign("appliedCampaign")
                .categoryId("categoryId")
                .productId("productId")
                .quantity("3")
                .totalAmount("100.0")
                .totalAmountAfterCampaign("80.0")
                .build();

        final CartResponse cartResponse = CartResponse.builder()
                .id("id")
                .items(List.of(cartItemResponse))
                .appliedCoupon("appliedCoupon")
                .totalAmount("100.00")
                .totalAmountAfterDiscount("75.0")
                .couponAmount("15.0")
                .totalAmountAfterCoupon("60.0")
                .deliveryCost("10.0")
                .build();

        // when
        when(cartManager.listAll()).thenReturn(List.of(cartResponse));

        final ResultActions resultActions = mockMvc.perform(get(CART_URL));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].id", is(cartResponse.getId())));
        resultActions.andExpect(jsonPath("$[0].appliedCoupon", is(cartResponse.getAppliedCoupon())));
        resultActions.andExpect(jsonPath("$[0].totalAmount", is(cartResponse.getTotalAmount())));
        resultActions.andExpect(jsonPath("$[0].totalAmountAfterDiscount", is(cartResponse.getTotalAmountAfterDiscount())));
        resultActions.andExpect(jsonPath("$[0].couponAmount", is(cartResponse.getCouponAmount())));
        resultActions.andExpect(jsonPath("$[0].totalAmountAfterCoupon", is(cartResponse.getTotalAmountAfterCoupon())));
        resultActions.andExpect(jsonPath("$[0].deliveryCost", is(cartResponse.getDeliveryCost())));
        resultActions.andExpect(jsonPath("$[0].items", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].items[0].appliedCampaign", is(cartItemResponse.getAppliedCampaign())));
        resultActions.andExpect(jsonPath("$[0].items[0].categoryId", is(cartItemResponse.getCategoryId())));
        resultActions.andExpect(jsonPath("$[0].items[0].productId", is(cartItemResponse.getProductId())));
        resultActions.andExpect(jsonPath("$[0].items[0].quantity", is(cartItemResponse.getQuantity())));
        resultActions.andExpect(jsonPath("$[0].items[0].totalAmount", is(cartItemResponse.getTotalAmount())));
        resultActions.andExpect(jsonPath("$[0].items[0].totalAmountAfterCampaign", is(cartItemResponse.getTotalAmountAfterCampaign())));

        verify(cartManager, times(1)).listAll();
    }

    @Test
    public void should_list_cart_when_cart_found_with_id() throws Exception {
        // given
        final Long cartId = 1L;

        final CartItemResponse cartItemResponse = CartItemResponse.builder()
                .appliedCampaign("appliedCampaign")
                .categoryId("categoryId")
                .productId("productId")
                .quantity("3")
                .totalAmount("100.0")
                .totalAmountAfterCampaign("80.0")
                .build();

        final CartResponse cartResponse = CartResponse.builder()
                .id("1")
                .items(List.of(cartItemResponse))
                .appliedCoupon("appliedCoupon")
                .totalAmount("100.00")
                .totalAmountAfterDiscount("75.0")
                .couponAmount("15.0")
                .totalAmountAfterCoupon("60.0")
                .deliveryCost("10.0")
                .build();

        // when
        when(cartManager.list(cartId)).thenReturn(cartResponse);

        final ResultActions resultActions = mockMvc.perform(
                get(StringUtils.join(CART_URL, "/{cartId}"), cartId));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(cartResponse.getId())));
        resultActions.andExpect(jsonPath("$.appliedCoupon", is(cartResponse.getAppliedCoupon())));
        resultActions.andExpect(jsonPath("$.totalAmount", is(cartResponse.getTotalAmount())));
        resultActions.andExpect(jsonPath("$.totalAmountAfterDiscount", is(cartResponse.getTotalAmountAfterDiscount())));
        resultActions.andExpect(jsonPath("$.couponAmount", is(cartResponse.getCouponAmount())));
        resultActions.andExpect(jsonPath("$.totalAmountAfterCoupon", is(cartResponse.getTotalAmountAfterCoupon())));
        resultActions.andExpect(jsonPath("$.deliveryCost", is(cartResponse.getDeliveryCost())));
        resultActions.andExpect(jsonPath("$.items", hasSize(1)));
        resultActions.andExpect(jsonPath("$.items[0].appliedCampaign", is(cartItemResponse.getAppliedCampaign())));
        resultActions.andExpect(jsonPath("$.items[0].categoryId", is(cartItemResponse.getCategoryId())));
        resultActions.andExpect(jsonPath("$.items[0].productId", is(cartItemResponse.getProductId())));
        resultActions.andExpect(jsonPath("$.items[0].quantity", is(cartItemResponse.getQuantity())));
        resultActions.andExpect(jsonPath("$.items[0].totalAmount", is(cartItemResponse.getTotalAmount())));
        resultActions.andExpect(jsonPath("$.items[0].totalAmountAfterCampaign", is(cartItemResponse.getTotalAmountAfterCampaign())));

        verify(cartManager, times(1)).list(cartId);
    }

    @Test
    public void should_delete_cart_when_cart_found_with_id() throws Exception {
        // given
        final Long cartId = 1L;

        // when
        final ResultActions resultActions = mockMvc.perform(
                delete(StringUtils.join(CART_URL, "/{cartId}"), cartId));

        // then
        resultActions.andExpect(status().isOk());
        verify(cartManager, times(1)).delete(cartId);
    }

    @Test
    public void should_return_updated_cart_when_add_item_to_the_cart() throws Exception {
        // given
        final Long cartId = 1L;

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("1")
                .quantity("2")
                .build();



        // when
        final ResultActions resultActions = mockMvc.perform(
                put(StringUtils.join(CART_URL, "/{cartId}/item"), cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebTestUtil.convertObjectToJsonBytes(cartAddItemRequest)));

        // then
        resultActions.andExpect(status().isOk());
        verify(cartManager, times(1)).addItem(cartId, cartAddItemRequest);
    }

    @Test
    public void should_return_bad_request_when_product_id_is_null() throws Exception {
        // given
        final String cartId = "1";

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .quantity("2")
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                put(StringUtils.join(CART_URL, "/{cartId}/item"), cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WebTestUtil.convertObjectToJsonBytes(cartAddItemRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_PRODUCT_ID)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_product_id_is_empty() throws Exception {
        // given
        final String cartId = "1";

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId(StringUtils.EMPTY)
                .quantity("2")
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                put(StringUtils.join(CART_URL, "/{cartId}/item"), cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WebTestUtil.convertObjectToJsonBytes(cartAddItemRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_PRODUCT_ID)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_product_id_is_blank() throws Exception {
        // given
        final String cartId = "1";

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId(StringUtils.SPACE)
                .quantity("2")
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                put(StringUtils.join(CART_URL, "/{cartId}/item"), cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WebTestUtil.convertObjectToJsonBytes(cartAddItemRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_PRODUCT_ID)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_quantity_is_null() throws Exception {
        // given
        final String cartId = "1";

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("1")
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                put(StringUtils.join(CART_URL, "/{cartId}/item"), cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WebTestUtil.convertObjectToJsonBytes(cartAddItemRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_QUANTITY)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_quantity_is_empty() throws Exception {
        // given
        final String cartId = "1";

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("1")
                .quantity(StringUtils.EMPTY)
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                put(StringUtils.join(CART_URL, "/{cartId}/item"), cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WebTestUtil.convertObjectToJsonBytes(cartAddItemRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_QUANTITY)).isTrue();
    }

    @Test
    public void should_return_bad_request_when_quantity_is_blank() throws Exception {
        // given
        final String cartId = "1";

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("1")
                .quantity(StringUtils.SPACE)
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                put(StringUtils.join(CART_URL, "/{cartId}/item"), cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(WebTestUtil.convertObjectToJsonBytes(cartAddItemRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
        assertThat(resultActions.andReturn().getResolvedException().getLocalizedMessage().contains(ERR_MISSING_QUANTITY)).isTrue();
    }

    @Test
    public void should_apply_coupon_with_given_cart_and_coupon_id() throws Exception {
        // given
        final Long cartId = 1L;
        final Long couponId = 2L;

        // when
        final ResultActions resultActions = mockMvc.perform(
                put(CART_URL + "/{cartId}/coupon/{couponId}", cartId, couponId));

        // then
        resultActions.andExpect(status().isOk());
        verify(cartManager, times(1)).applyCoupon(cartId, couponId);
    }
}