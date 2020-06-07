package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CartToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import com.ibrahimtugrul.cartservice.domain.entity.CartItem;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CartRepository;
import com.ibrahimtugrul.cartservice.domain.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartToVoConverter cartToVoConverter;
    private final DiscountCalculatorService discountCalculatorService;

    public Long create() {
        final Cart cart = Cart.builder()
                .build();
        cartRepository.save(cart);
        return cart.getId();
    }

    public List<CartVo> listAll() {
        List<Cart> cartList = cartRepository.findAll();
        return cartList.stream().map(cart -> cartToVoConverter.convert(cart)).collect(Collectors.toList());
    }

    public CartVo retrieve(final Long cartId) {
        final Cart cart = retrieveCartUnsafe(cartId).orElseThrow(() -> new EntityNotFoundException("cart"));
        return cartToVoConverter.convert(cart);
    }

    public void delete(final Long cartId) {
        final Cart cart = retrieveCartUnsafe(cartId).orElseThrow(() -> new EntityNotFoundException("cart"));
        cartRepository.delete(cart);
    }

    private Optional<Cart> retrieveCartUnsafe(final Long cartId) {
        return cartRepository.findById(cartId);
    }

    public void addItem(final Long cartId, final CartAddItemVo cartAddItemVo) {
        final Cart cart = retrieveCartUnsafe(cartId).orElseThrow(() -> new EntityNotFoundException("cart"));
        final CartItem cartItem = createCartItem(cart, cartAddItemVo);
        removeExistingItem(cart, cartAddItemVo);
        cart.getItems().add(cartItem);
        cart.setTotalAmount(cart.getItems().stream().collect(Collectors.summarizingDouble(CartItem::getTotalAmount)).getSum());
        cart.setTotalAmountAfterDiscount(cart.getItems().stream().collect(Collectors.summarizingDouble(CartItem::getTotalAmountAfterCampaign)).getSum());
        cartRepository.save(cart);
    }

    private void removeExistingItem(final Cart cart, final CartAddItemVo cartAddItemVo) {
        final Optional<CartItem> cartItem = cart.getItems().stream().filter(cartItem1 -> cartAddItemVo.getProductId().equals(cartItem1.getProductId())).findFirst();
        if(cartItem.isPresent()) {
            cart.getItems().remove(cartItem.get());
        }
    }

    private CartItem createCartItem(final Cart cart, final CartAddItemVo cartAddItemVo) {
        final CartItem cartItem = createOrUpdateCartItem(cart, cartAddItemVo);
        return calculateCartItemInfoWithCampaign(cartItem);
    }

    private CartItem createOrUpdateCartItem(final Cart cart, final CartAddItemVo cartAddItemVo) {
        return cart.getItems().stream()
                .filter(cartItem -> cartAddItemVo.getProductId().equals(cartItem.getProductId())).findFirst()
                .map(cartItem -> {return CartItem.builder()
                                .productId(cartAddItemVo.getProductId())
                                .quantity(cartItem.getQuantity() + cartAddItemVo.getQuantity())
                                .build();})
                .orElse(CartItem.builder()
                                .productId(cartAddItemVo.getProductId())
                                .quantity(cartAddItemVo.getQuantity())
                                .build());
    }

    private CartItem calculateCartItemInfoWithCampaign(final CartItem cartItem) {
        return discountCalculatorService.createCartItemWithCampaign(cartItem);
    }
}