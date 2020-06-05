package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CartItemResponse;
import com.ibrahimtugrul.cartservice.domain.vo.CartItemVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartItemVoToCartItemResponseMapperTest {

    private CartItemVoToCartItemResponseMapper cartItemVoToCartItemResponseMapper;

    @BeforeEach
    public void setup() {
        this.cartItemVoToCartItemResponseMapper = new CartItemVoToCartItemResponseMapper();
    }

    @Test
    public void should_map() {
        // given
        final CartItemVo cartItemVo = CartItemVo.builder()
                .appliedCampaign(2L)
                .categoryId(2L)
                .productId(3L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        // when
        final CartItemResponse cartItemResponse = cartItemVoToCartItemResponseMapper.apply(cartItemVo);

        // then
        assertThat(cartItemResponse).isNotNull();
        assertThat(cartItemResponse.getAppliedCampaign()).isEqualTo(String.valueOf(cartItemVo.getAppliedCampaign()));
        assertThat(cartItemResponse.getCategoryId()).isEqualTo(String.valueOf(cartItemVo.getCategoryId()));
        assertThat(cartItemResponse.getProductId()).isEqualTo(String.valueOf(cartItemVo.getProductId()));
        assertThat(cartItemResponse.getQuantity()).isEqualTo(String.valueOf(cartItemVo.getQuantity()));
        assertThat(cartItemResponse.getTotalAmount()).isEqualTo(String.valueOf(cartItemVo.getTotalAmount()));
        assertThat(cartItemResponse.getTotalAmountAfterCampaign()).isEqualTo(String.valueOf(cartItemVo.getTotalAmountAfterCampaign()));
    }
}