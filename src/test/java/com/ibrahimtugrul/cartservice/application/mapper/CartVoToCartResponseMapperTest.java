package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CartItemResponse;
import com.ibrahimtugrul.cartservice.application.model.response.CartResponse;
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
public class CartVoToCartResponseMapperTest {

    @Mock
    private CartItemVoToCartItemResponseMapper cartItemVoToCartItemResponseMapper;

    private CartVoToCartResponseMapper cartVoToCartResponseMapper;

    @BeforeEach
    public void setup() {
        this.cartVoToCartResponseMapper = new CartVoToCartResponseMapper(cartItemVoToCartItemResponseMapper);
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

        final CartItemResponse cartItemResponse = CartItemResponse.builder()
                .appliedCampaign("2")
                .categoryId("2")
                .productId("3")
                .quantity("4")
                .totalAmount("100.0")
                .totalAmountAfterCampaign("75.0")
                .build();

        final CartVo cartVo = CartVo.builder()
                .items(List.of(cartItemVo))
                .appliedCoupon(1L)
                .id(2L)
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        // when
        when(cartItemVoToCartItemResponseMapper.apply(cartItemVo)).thenReturn(cartItemResponse);

        final CartResponse cartResponse = cartVoToCartResponseMapper.apply(cartVo);

        // then
        assertThat(cartResponse).isNotNull();
        assertThat(cartResponse.getAppliedCoupon()).isEqualTo(String.valueOf(cartVo.getAppliedCoupon()));
        assertThat(cartResponse.getId()).isEqualTo(String.valueOf(cartVo.getId()));
        assertThat(cartResponse.getTotalAmount()).isEqualTo(String.valueOf(cartVo.getTotalAmount()));
        assertThat(cartResponse.getTotalAmountAfterDiscount()).isEqualTo(String.valueOf(cartVo.getTotalAmountAfterDiscount()));
        assertThat(cartResponse.getItems()).isNotEmpty();
        assertThat(cartResponse.getItems().size()).isEqualTo(1);
        assertThat(cartResponse.getItems().get(0).getTotalAmountAfterCampaign()).isEqualTo(String.valueOf(cartItemVo.getTotalAmountAfterCampaign()));
        assertThat(cartResponse.getItems().get(0).getTotalAmount()).isEqualTo(String.valueOf(cartItemVo.getTotalAmount()));
        assertThat(cartResponse.getItems().get(0).getProductId()).isEqualTo(String.valueOf(cartItemVo.getProductId()));
        assertThat(cartResponse.getItems().get(0).getCategoryId()).isEqualTo(String.valueOf(cartItemVo.getCategoryId()));
        assertThat(cartResponse.getItems().get(0).getAppliedCampaign()).isEqualTo(String.valueOf(cartItemVo.getAppliedCampaign()));
        assertThat(cartResponse.getItems().get(0).getQuantity()).isEqualTo(String.valueOf(cartItemVo.getQuantity()));
    }
}