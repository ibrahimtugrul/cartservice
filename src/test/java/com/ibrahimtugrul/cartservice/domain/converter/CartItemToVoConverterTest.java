package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.vo.CartItemVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartItemToVoConverterTest {

    private CartItemToVoConverter cartItemToVoConverter;

    @BeforeEach
    public void setup() {
        this.cartItemToVoConverter = new CartItemToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        // when
        final CartItemVo cartItemVo = cartItemToVoConverter.convert(cartItem);

        // then
        assertThat(cartItemVo).isNotNull();
        assertThat(cartItemVo.getAppliedCampaign()).isEqualTo(cartItem.getAppliedCampaign());
        assertThat(cartItemVo.getCategoryId()).isEqualTo(cartItem.getCategoryId());
        assertThat(cartItemVo.getProductId()).isEqualTo(cartItem.getProductId());
        assertThat(cartItemVo.getQuantity()).isEqualTo(cartItem.getQuantity());
        assertThat(cartItemVo.getTotalAmount()).isEqualTo(cartItem.getTotalAmount());
        assertThat(cartItemVo.getTotalAmountAfterCampaign()).isEqualTo(cartItem.getTotalAmountAfterCampaign());
    }
}