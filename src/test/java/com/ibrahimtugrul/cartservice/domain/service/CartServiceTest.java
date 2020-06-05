package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CartToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CartRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CartItemVo;
import com.ibrahimtugrul.cartservice.domain.vo.CartVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Captor
    private ArgumentCaptor<Cart> cartArgumentCaptor;

    @Mock
    private CartToVoConverter cartToVoConverter;

    private static final String ERR_ENTITY_NOT_FOUND = "domain.entity.notFound";

    @BeforeEach
    public void setup() {
        this.cartService = new CartService(cartRepository, cartToVoConverter);
    }

    @Test
    public void should_save_cart() {
        // given
        final Long cartId = 1L;

        // when
        when(cartRepository.save(any(Cart.class))).thenAnswer(new Answer<Cart>() {
            @Override
            public Cart answer(InvocationOnMock invocation) {
                Cart cart = (Cart) invocation.getArguments()[0];
                cart.setId(cartId);
                return cart;
            }
        });

        final long returnedCartId = cartService.create();

        // then
        cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        final InOrder inOrder = Mockito.inOrder(cartRepository);
        inOrder.verify(cartRepository).save(cartArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(cartArgumentCaptor.getValue()).isNotNull();

        final Cart savedCart = cartArgumentCaptor.getValue();

        assertThat(savedCart.getId()).isEqualTo(returnedCartId);
    }

    @Test
    public void should_list_all_categories() {
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

        final CartVo cartVo = CartVo.builder()
                .appliedCoupon(2L)
                .id(3L)
                .items(List.of(cartItemVo))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        // when
        when(cartRepository.findAll()).thenReturn(List.of(cart));
        when(cartToVoConverter.convert(cart)).thenReturn(cartVo);

        final List<CartVo> cartVoList = cartService.listAll();

        // then
        cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        final InOrder inOrder = Mockito.inOrder(cartRepository, cartToVoConverter);
        inOrder.verify(cartRepository).findAll();
        inOrder.verify(cartToVoConverter).convert(cartArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(cartVoList).isNotEmpty();
        assertThat(cartVoList.size()).isEqualTo(1);
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

    @Test
    public void should_retrieve_cart_when_cart_found() {
        // given
        final Long id = 1L;
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

        final CartVo cartVo = CartVo.builder()
                .appliedCoupon(2L)
                .id(3L)
                .items(List.of(cartItemVo))
                .totalAmount(100.0)
                .totalAmountAfterDiscount(75.0)
                .build();

        // when
        when(cartRepository.findById(id)).thenReturn(Optional.of(cart));
        when(cartToVoConverter.convert(cart)).thenReturn(cartVo);

        final CartVo returnedCartVo = cartService.retrieve(id);

        // then
        cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        final InOrder inOrder = Mockito.inOrder(cartRepository, cartToVoConverter);
        inOrder.verify(cartRepository).findById(id);
        inOrder.verify(cartToVoConverter).convert(cartArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

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

    @Test
    public void should_throw_exception_when_cart_not_found() {
        // given
        final Long id = 1L;

        // when
        when(cartRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{cartService.retrieve(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }

    @Test
    public void should_delete_cart_when_cart_found() {
        // given
        final Long id = 1L;
        final CartItem cartItem = CartItem.builder()
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
        when(cartRepository.findById(id)).thenReturn(Optional.of(cart));

        cartService.delete(id);

        // then
        cartArgumentCaptor = ArgumentCaptor.forClass(Cart.class);

        final InOrder inOrder = Mockito.inOrder(cartRepository);
        inOrder.verify(cartRepository).findById(id);
        inOrder.verify(cartRepository).delete(cartArgumentCaptor.capture());
        inOrder.verifyNoMoreInteractions();

        assertThat(cartArgumentCaptor.getValue()).isNotNull();

        final Cart foundedCart = cartArgumentCaptor.getValue();

        assertThat(foundedCart.getAppliedCoupon()).isEqualTo(cart.getAppliedCoupon());
        assertThat(foundedCart.getId()).isEqualTo(cart.getId());
        assertThat(foundedCart.getTotalAmount()).isEqualTo(cart.getTotalAmount());
        assertThat(foundedCart.getTotalAmountAfterDiscount()).isEqualTo(cart.getTotalAmountAfterDiscount());
        assertThat(foundedCart.getItems()).isNotEmpty();
        assertThat(foundedCart.getItems().size()).isEqualTo(1);
        assertThat(foundedCart.getItems().get(0).getTotalAmountAfterCampaign()).isEqualTo(cart.getItems().get(0).getTotalAmountAfterCampaign());
        assertThat(foundedCart.getItems().get(0).getTotalAmount()).isEqualTo(cart.getItems().get(0).getTotalAmount());
        assertThat(foundedCart.getItems().get(0).getQuantity()).isEqualTo(cart.getItems().get(0).getQuantity());
        assertThat(foundedCart.getItems().get(0).getProductId()).isEqualTo(cart.getItems().get(0).getProductId());
        assertThat(foundedCart.getItems().get(0).getCategoryId()).isEqualTo(cart.getItems().get(0).getCategoryId());
        assertThat(foundedCart.getItems().get(0).getAppliedCampaign()).isEqualTo(cart.getItems().get(0).getAppliedCampaign());
    }

    @Test
    public void should_throw_exception_when_delete_cart_not_found() {
        // given
        final Long id = 1L;

        // when
        when(cartRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        final Throwable throwable = catchThrowable(()->{cartService.delete(id);});

        // then
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);

        final EntityNotFoundException entityNotFoundException = (EntityNotFoundException) throwable;
        assertThat(entityNotFoundException.getLocalizedMessage().contains(ERR_ENTITY_NOT_FOUND)).isTrue();
    }
}