package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.vo.CartItemVo;
import com.ibrahimtugrul.cartservice.domain.vo.CartVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartToVoConverterTest {

    @Mock
    private CartItemToVoConverter cartItemToVoConverter;

    private CartToVoConverter cartToVoConverter;

    @BeforeEach
    public void setup() {
        this.cartToVoConverter = new CartToVoConverter(cartItemToVoConverter);
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

        final CartItemVo cartItemVo = CartItemVo.builder()
                .appliedCampaign(2L)
                .categoryId(3L)
                .productId(1L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final Cart cart = Cart.builder()
                .appliedCoupon(2L)
                .id(3L)
                .items(List.of(cartItem))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        // when
        when(cartItemToVoConverter.convert(cartItem)).thenReturn(cartItemVo);

        final CartVo cartVo = cartToVoConverter.convert(cart);

        // then
        assertThat(cartVo).isNotNull();
        assertThat(cartVo.getAppliedCoupon()).isEqualTo(cart.getAppliedCoupon());
        assertThat(cartVo.getId()).isEqualTo(cart.getId());
        assertThat(cartVo.getTotalAmount()).isEqualTo(cart.getTotalAmount());
        assertThat(cartVo.getTotalAmountAfterDiscount()).isEqualTo(cart.getTotalAmountAfterDiscount());
        assertThat(cartVo.getItems()).isNotEmpty();
        assertThat(cartVo.getItems().size()).isEqualTo(1);
        assertThat(cartVo.getItems().get(0).getTotalAmountAfterCampaign()).isEqualTo(cart.getItems().get(0).getTotalAmountAfterCampaign());
        assertThat(cartVo.getItems().get(0).getTotalAmount()).isEqualTo(cart.getItems().get(0).getTotalAmount());
        assertThat(cartVo.getItems().get(0).getQuantity()).isEqualTo(cart.getItems().get(0).getQuantity());
        assertThat(cartVo.getItems().get(0).getProductId()).isEqualTo(cart.getItems().get(0).getProductId());
        assertThat(cartVo.getItems().get(0).getCategoryId()).isEqualTo(cart.getItems().get(0).getCategoryId());
        assertThat(cartVo.getItems().get(0).getAppliedCampaign()).isEqualTo(cart.getItems().get(0).getAppliedCampaign());
    }
}