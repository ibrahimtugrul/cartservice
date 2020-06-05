package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.vo.CartItemVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CartItemToVoConverter implements Function<CartItem, CartItemVo>, Converter<CartItem, CartItemVo> {
    @Override
    public CartItemVo apply(final CartItem cartItem) {
        return CartItemVo.builder()
                .totalAmountAfterCampaign(cartItem.getTotalAmountAfterCampaign())
                .totalAmount(cartItem.getTotalAmount())
                .quantity(cartItem.getQuantity())
                .productId(cartItem.getProductId())
                .categoryId(cartItem.getCategoryId())
                .appliedCampaign(cartItem.getAppliedCampaign())
                .build();
    }

    @Override
    public CartItemVo convert(final CartItem cartItem) {
        return apply(cartItem);
    }
}