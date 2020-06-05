package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CartItemResponse;
import com.ibrahimtugrul.cartservice.application.model.response.CartResponse;
import com.ibrahimtugrul.cartservice.domain.vo.CartVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartVoToCartResponseMapper implements Function<CartVo, CartResponse> {

    private final CartItemVoToCartItemResponseMapper cartItemVoToCartItemResponseMapper;
    @Override
    public CartResponse apply(final CartVo cartVo) {
        final List<CartItemResponse> cartItemResponseList = cartVo.getItems().stream()
                .map(cartItemVo -> cartItemVoToCartItemResponseMapper.apply(cartItemVo)).collect(Collectors.toList());

        return CartResponse.builder()
                .items(cartItemResponseList)
                .totalAmountAfterDiscount(String.valueOf(cartVo.getTotalAmountAfterDiscount()))
                .totalAmount(String.valueOf(cartVo.getTotalAmount()))
                .appliedCoupon(String.valueOf(cartVo.getAppliedCoupon()))
                .id(String.valueOf(cartVo.getId()))
                .build();
    }
}