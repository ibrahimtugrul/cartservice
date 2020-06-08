package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.enums.IConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliveryCostCalculatorTest {

    private DeliveryCostCalculator deliveryCostCalculator;

    @BeforeEach
    public void setup() {
        this.deliveryCostCalculator = new DeliveryCostCalculator();
    }

    @Test
    public void should_calculate_delivery_cost() {
        // given
        final CartItem cartItem = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final CartItem cartItem1 = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(2L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final CartItem cartItem2 = CartItem.builder()
                .appliedCampaign(2L)
                .categoryId(2L)
                .productId(3L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .id(3L)
                .items(List.of(cartItem, cartItem1, cartItem2))
                .totalAmount(300.0)
                .totalAmountAfterDiscount(225.0)
                .build();

        // when
        final double deliveryCost = deliveryCostCalculator.calculateDeliveryCost(cart);

        // then
        assertThat(deliveryCost).isEqualTo((3 * IConstants.PRODUCT_COST) + (2 * IConstants.DELIVERY_COST) + IConstants.FIXED_DELIVERY_COST);
    }
}