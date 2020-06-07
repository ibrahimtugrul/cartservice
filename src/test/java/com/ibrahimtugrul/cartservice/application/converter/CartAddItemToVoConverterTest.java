package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import com.ibrahimtugrul.cartservice.domain.vo.CartAddItemVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartAddItemToVoConverterTest {

    private CartAddItemToVoConverter cartAddItemToVoConverter;

    @BeforeEach
    public void setup() {
        this.cartAddItemToVoConverter = new CartAddItemToVoConverter();
    }

    @Test
    public void should_convert() {
        // given
        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .quantity("2")
                .productId("3")
                .build();

        // when
        final CartAddItemVo cartAddItemVo = cartAddItemToVoConverter.convert(cartAddItemRequest);

        // then
        assertThat(cartAddItemVo).isNotNull();
        assertThat(cartAddItemVo.getProductId()).isEqualTo(Long.valueOf(cartAddItemRequest.getProductId()));
        assertThat(cartAddItemVo.getQuantity()).isEqualTo(Long.valueOf(cartAddItemRequest.getQuantity()));
    }
}