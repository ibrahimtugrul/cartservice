package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.CartAddItemToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.CartVoToCartResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CartItemResponse;
import com.ibrahimtugrul.cartservice.application.model.response.CartResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.application.validator.CartAddItemValidator;
import com.ibrahimtugrul.cartservice.domain.service.CartService;
import com.ibrahimtugrul.cartservice.domain.vo.CartAddItemVo;
import com.ibrahimtugrul.cartservice.domain.vo.CartItemVo;
import com.ibrahimtugrul.cartservice.domain.vo.CartVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartManagerTest {

    private CartManager cartManager;

    @Mock
    private CartService cartService;

    @Mock
    private CartVoToCartResponseMapper cartVoToCartResponseMapper;

    @Mock
    private CartAddItemValidator cartAddItemValidator;

    @Mock
    private CartAddItemToVoConverter cartAddItemToVoConverter;

    @BeforeEach
    public void setup() {
        this.cartManager = new CartManager(cartService, cartVoToCartResponseMapper, cartAddItemValidator, cartAddItemToVoConverter);
    }

    @Test
    public void should_return_created_cart_id_when_cart_create_request_is_valid() {
        // given
        final long cartId = 1L;

        // when
        when(cartService.create()).thenReturn(cartId);

        final IdResponse responseEntity = cartManager.create();

        // then
        final InOrder inOrder = Mockito.inOrder(cartService);
        inOrder.verify(cartService).create();
        inOrder.verifyNoMoreInteractions();

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getId()).isEqualTo(String.valueOf(cartId));
    }

    @Test
    public void should_return_cart_list() {
        // given
        final CartItemVo cartItemVo = CartItemVo.builder()
                .appliedCampaign(2L)
                .categoryId(2L)
                .productId(3L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final CartVo cartVo = CartVo.builder()
                .items(List.of(cartItemVo))
                .appliedCoupon(1L)
                .id(2L)
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        final CartItemResponse cartItemResponse = CartItemResponse.builder()
                .appliedCampaign("2")
                .categoryId("2")
                .productId("3")
                .quantity("4")
                .totalAmount("100.0")
                .totalAmountAfterCampaign("75.0")
                .build();

        final CartResponse cartResponse = CartResponse.builder()
                .items(List.of(cartItemResponse))
                .appliedCoupon("1")
                .id("2")
                .totalAmount("100.0")
                .totalAmountAfterDiscount("75.0")
                .build();

        // when
        when(cartService.listAll()).thenReturn(List.of(cartVo));
        when(cartVoToCartResponseMapper.apply(cartVo)).thenReturn(cartResponse);

        final List<CartResponse> cartResponseList = cartManager.listAll();

        // then
        final InOrder inOrder = Mockito.inOrder(cartService, cartVoToCartResponseMapper);
        inOrder.verify(cartService).listAll();
        inOrder.verify(cartVoToCartResponseMapper).apply(cartVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(cartResponseList).isNotEmpty();
        assertThat(cartResponseList.size()).isEqualTo(1);
        assertThat(cartResponseList.get(0).getTotalAmountAfterDiscount()).isEqualTo(String.valueOf(cartVo.getTotalAmountAfterDiscount()));
        assertThat(cartResponseList.get(0).getId()).isEqualTo(String.valueOf(cartVo.getId()));
        assertThat(cartResponseList.get(0).getTotalAmount()).isEqualTo(String.valueOf(cartVo.getTotalAmount()));
        assertThat(cartResponseList.get(0).getAppliedCoupon()).isEqualTo(String.valueOf(cartVo.getAppliedCoupon()));
        assertThat(cartResponseList.get(0).getItems()).isNotEmpty();
        assertThat(cartResponseList.get(0).getItems().size()).isEqualTo(1);
        assertThat(cartResponseList.get(0).getItems().get(0).getQuantity()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getQuantity()));
        assertThat(cartResponseList.get(0).getItems().get(0).getAppliedCampaign()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getAppliedCampaign()));
        assertThat(cartResponseList.get(0).getItems().get(0).getCategoryId()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getCategoryId()));
        assertThat(cartResponseList.get(0).getItems().get(0).getProductId()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getProductId()));
        assertThat(cartResponseList.get(0).getItems().get(0).getTotalAmount()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getTotalAmount()));
        assertThat(cartResponseList.get(0).getItems().get(0).getTotalAmountAfterCampaign()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getTotalAmountAfterCampaign()));
    }

    @Test
    public void should_return_cart_when_cart_found_byId() {
        // given
        final Long id = 1L;
        // given
        final CartItemVo cartItemVo = CartItemVo.builder()
                .appliedCampaign(2L)
                .categoryId(2L)
                .productId(3L)
                .quantity(4L)
                .totalAmount(100.0)
                .totalAmountAfterCampaign(75.0)
                .build();

        final CartVo cartVo = CartVo.builder()
                .items(List.of(cartItemVo))
                .appliedCoupon(1L)
                .id(2L)
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        final CartItemResponse cartItemResponse = CartItemResponse.builder()
                .appliedCampaign("2")
                .categoryId("2")
                .productId("3")
                .quantity("4")
                .totalAmount("100.0")
                .totalAmountAfterCampaign("75.0")
                .build();

        final CartResponse cartResponse = CartResponse.builder()
                .items(List.of(cartItemResponse))
                .appliedCoupon("1")
                .id("2")
                .totalAmount("100.0")
                .totalAmountAfterDiscount("75.0")
                .build();

        // when
        when(cartService.retrieve(id)).thenReturn(cartVo);
        when(cartVoToCartResponseMapper.apply(cartVo)).thenReturn(cartResponse);

        final CartResponse cartResponse1 = cartManager.list(id);

        // then
        final InOrder inOrder = Mockito.inOrder(cartService, cartVoToCartResponseMapper);
        inOrder.verify(cartService).retrieve(id);
        inOrder.verify(cartVoToCartResponseMapper).apply(cartVo);
        inOrder.verifyNoMoreInteractions();

        assertThat(cartResponse1).isNotNull();
        assertThat(cartResponse1.getTotalAmountAfterDiscount()).isEqualTo(String.valueOf(cartVo.getTotalAmountAfterDiscount()));
        assertThat(cartResponse1.getId()).isEqualTo(String.valueOf(cartVo.getId()));
        assertThat(cartResponse1.getTotalAmount()).isEqualTo(String.valueOf(cartVo.getTotalAmount()));
        assertThat(cartResponse1.getAppliedCoupon()).isEqualTo(String.valueOf(cartVo.getAppliedCoupon()));
        assertThat(cartResponse1.getItems()).isNotEmpty();
        assertThat(cartResponse1.getItems().size()).isEqualTo(1);
        assertThat(cartResponse1.getItems().get(0).getQuantity()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getQuantity()));
        assertThat(cartResponse1.getItems().get(0).getAppliedCampaign()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getAppliedCampaign()));
        assertThat(cartResponse1.getItems().get(0).getCategoryId()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getCategoryId()));
        assertThat(cartResponse1.getItems().get(0).getProductId()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getProductId()));
        assertThat(cartResponse1.getItems().get(0).getTotalAmount()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getTotalAmount()));
        assertThat(cartResponse1.getItems().get(0).getTotalAmountAfterCampaign()).isEqualTo(String.valueOf(cartVo.getItems().get(0).getTotalAmountAfterCampaign()));

    }

    @Test
    public void should_delete_cart_when_cart_found() {
        // given
        final Long id = 1L;

        // when
        cartManager.delete(id);

        // then
        final InOrder inOrder = Mockito.inOrder(cartService);
        inOrder.verify(cartService).delete(id);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void should_add_item_when_add_item_request_is_valid() {
        // given
        final Long cartId = 1L;

        final CartAddItemRequest cartAddItemRequest = CartAddItemRequest.builder()
                .productId("1")
                .quantity("1")
                .build();

        final CartAddItemVo cartAddItemVo = CartAddItemVo.builder()
                .productId(1L)
                .quantity(1L)
                .build();

        // when
        when(cartAddItemToVoConverter.convert(cartAddItemRequest)).thenReturn(cartAddItemVo);

        cartManager.addItem(cartId, cartAddItemRequest);

        // then
        final InOrder inOrder = Mockito.inOrder(cartAddItemValidator, cartAddItemToVoConverter, cartService);
        inOrder.verify(cartAddItemValidator).validate(cartAddItemRequest);
        inOrder.verify(cartAddItemToVoConverter).convert(cartAddItemRequest);
        inOrder.verify(cartService).addItem(cartId, cartAddItemVo);
        inOrder.verifyNoMoreInteractions();
    }
}