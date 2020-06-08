package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.service.DeliveryCostCalculator;
import com.ibrahimtugrul.cartservice.domain.vo.CartItemVo;
import com.ibrahimtugrul.cartservice.domain.vo.CartVo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartToVoConverter implements Function<Cart, CartVo>, Converter<Cart, CartVo> {

    private final CartItemToVoConverter cartItemToVoConverter;
    private final DeliveryCostCalculator deliveryCostCalculator;

    @Override
    public CartVo apply(final Cart cart) {
        final List<CartItemVo> cartItemVoList = cart.getItems().stream()
                .map(cartItem -> cartItemToVoConverter.convert(cartItem)).collect(Collectors.toList());

        return CartVo.builder()
                .totalAmountAfterDiscount(cart.getTotalAmountAfterDiscount())
                .totalAmount(cart.getTotalAmount())
                .id(cart.getId())
                .appliedCoupon(cart.getAppliedCoupon())
                .items(cartItemVoList)
                .couponAmount(cart.getCouponAmount())
                .totalAmountAfterCoupon(cart.getTotalAmountAfterCoupon())
                .deliveryCost(deliveryCostCalculator.calculateDeliveryCost(cart))
                .build();
    }

    @Override
    public CartVo convert(final Cart cart) {
        return apply(cart);
    }
}