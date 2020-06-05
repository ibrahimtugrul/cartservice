package com.ibrahimtugrul.cartservice.infrastructure.rest.integration;

import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.repository.CartRepository;
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

public class RestCartControllerIT extends BaseWebIT {
    @Autowired
    private CartRepository cartRepository;

    private static final String CART_URL = "/api/v1/cart";

    @BeforeEach
    public void setup() {// to delete initial values for integration test health
        cartRepository.deleteAll();
    }

    @Test
    public void should_save_cart() throws Exception {
        // given

        // when
        final ResultActions resultActions = mockMvc.perform(post(CART_URL));

        // then
        resultActions.andExpect(status().isOk());

        final List<Cart> cartList = cartRepository.findAll();

        assertThat(cartList).isNotEmpty();
        assertThat(cartList.size()).isEqualTo(1);

        final Cart savedCart = cartList.get(0);

        assertThat(savedCart.getAppliedCoupon()).isEqualTo(0L);
        assertThat(savedCart.getTotalAmount()).isEqualTo(0.0);
        assertThat(savedCart.getTotalAmountAfterDiscount()).isEqualTo(0.0);
        assertThat(savedCart.getItems()).isEmpty();

        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(savedCart.getId()))));
    }

    @Test
    public void should_return_cart_list() throws Exception {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .items(List.of(cartItem))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        cartRepository.save(cart);

        // when
        final ResultActions resultActions = mockMvc.perform(get(CART_URL)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].id", is(String.valueOf(cart.getId()))));
        resultActions.andExpect(jsonPath("$[0].appliedCoupon", is(String.valueOf(cart.getAppliedCoupon()))));
        resultActions.andExpect(jsonPath("$[0].totalAmount", is(String.valueOf(cart.getTotalAmount()))));
        resultActions.andExpect(jsonPath("$[0].totalAmountAfterDiscount", is(String.valueOf(cart.getTotalAmountAfterDiscount()))));
        resultActions.andExpect(jsonPath("$[0].items", hasSize(1)));
        resultActions.andExpect(jsonPath("$[0].items[0].appliedCampaign", is((String.valueOf(cart.getItems().get(0).getAppliedCampaign())))));
        resultActions.andExpect(jsonPath("$[0].items[0].categoryId", is((String.valueOf(cart.getItems().get(0).getCategoryId())))));
        resultActions.andExpect(jsonPath("$[0].items[0].productId", is((String.valueOf(cart.getItems().get(0).getProductId())))));
        resultActions.andExpect(jsonPath("$[0].items[0].quantity", is((String.valueOf(cart.getItems().get(0).getQuantity())))));
        resultActions.andExpect(jsonPath("$[0].items[0].totalAmount", is((String.valueOf(cart.getItems().get(0).getTotalAmount())))));
        resultActions.andExpect(jsonPath("$[0].items[0].totalAmountAfterCampaign", is((String.valueOf(cart.getItems().get(0).getTotalAmountAfterCampaign())))));
    }

    @Test
    public void should_return_cart_when_cart_found() throws Exception {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .items(List.of(cartItem))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        cartRepository.save(cart);

        // when
        final ResultActions resultActions = mockMvc.perform(get(StringUtils.join(CART_URL, "/{cartId}")
                , cart.getId()));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id", is(String.valueOf(cart.getId()))));
        resultActions.andExpect(jsonPath("$.appliedCoupon", is(String.valueOf(cart.getAppliedCoupon()))));
        resultActions.andExpect(jsonPath("$.totalAmount", is(String.valueOf(cart.getTotalAmount()))));
        resultActions.andExpect(jsonPath("$.totalAmountAfterDiscount", is(String.valueOf(cart.getTotalAmountAfterDiscount()))));
        resultActions.andExpect(jsonPath("$.items", hasSize(1)));
        resultActions.andExpect(jsonPath("$.items[0].appliedCampaign", is((String.valueOf(cart.getItems().get(0).getAppliedCampaign())))));
        resultActions.andExpect(jsonPath("$.items[0].categoryId", is((String.valueOf(cart.getItems().get(0).getCategoryId())))));
        resultActions.andExpect(jsonPath("$.items[0].productId", is((String.valueOf(cart.getItems().get(0).getProductId())))));
        resultActions.andExpect(jsonPath("$.items[0].quantity", is((String.valueOf(cart.getItems().get(0).getQuantity())))));
        resultActions.andExpect(jsonPath("$.items[0].totalAmount", is((String.valueOf(cart.getItems().get(0).getTotalAmount())))));
        resultActions.andExpect(jsonPath("$.items[0].totalAmountAfterCampaign", is((String.valueOf(cart.getItems().get(0).getTotalAmountAfterCampaign())))));

    }

    @Test
    public void should_delete_cart_when_cart_found() throws  Exception {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .items(List.of(cartItem))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        cartRepository.save(cart);

        // when
        final ResultActions resultActions = mockMvc.perform(delete(StringUtils.join(CART_URL, "/{cartId}")
                , cart.getId()));

        // then
        resultActions.andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        cartRepository.deleteAll();
    }
}