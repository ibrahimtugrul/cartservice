package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CartItemResponse;
import com.ibrahimtugrul.cartservice.domain.vo.CartItemVo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CartItemVoToCartItemResponseMapper implements Function<CartItemVo, CartItemResponse> {
    @Override
    public CartItemResponse apply(final CartItemVo cartItemVo) {
        return CartItemResponse.builder()
                .totalAmountAfterCampaign(String.valueOf(cartItemVo.getTotalAmountAfterCampaign()))
                .totalAmount(String.valueOf(cartItemVo.getTotalAmount()))
                .quantity(String.valueOf(cartItemVo.getQuantity()))
                .productId(String.valueOf(cartItemVo.getProductId()))
                .categoryId(String.valueOf(cartItemVo.getCategoryId()))
                .appliedCampaign(String.valueOf(cartItemVo.getAppliedCampaign()))
                .build();
    }
}
