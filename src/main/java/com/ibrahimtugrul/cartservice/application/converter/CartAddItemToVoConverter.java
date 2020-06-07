package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import com.ibrahimtugrul.cartservice.domain.vo.CartAddItemVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CartAddItemToVoConverter implements Function<CartAddItemRequest, CartAddItemVo>, Converter<CartAddItemRequest, CartAddItemVo> {
    @Override
    public CartAddItemVo apply(final CartAddItemRequest cartAddItemRequest) {
        return CartAddItemVo.builder()
                .productId(Long.valueOf(cartAddItemRequest.getProductId()))
                .quantity(Long.valueOf(cartAddItemRequest.getQuantity()))
                .build();
    }

    @Override
    public CartAddItemVo convert(final CartAddItemRequest cartAddItemRequest) {
        return apply(cartAddItemRequest);
    }
}