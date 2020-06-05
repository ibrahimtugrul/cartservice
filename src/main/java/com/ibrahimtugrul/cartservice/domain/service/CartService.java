package com.ibrahimtugrul.cartservice.domain.service;

import com.ibrahimtugrul.cartservice.domain.converter.CampaignToVoConverter;
import com.ibrahimtugrul.cartservice.domain.converter.CartToVoConverter;
import com.ibrahimtugrul.cartservice.domain.entity.Cart;
import com.ibrahimtugrul.cartservice.domain.exception.EntityNotFoundException;
import com.ibrahimtugrul.cartservice.domain.repository.CampaignRepository;
import com.ibrahimtugrul.cartservice.domain.repository.CartRepository;
import com.ibrahimtugrul.cartservice.domain.vo.CartVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartToVoConverter cartToVoConverter;

    public Long create() {
        final Cart cart = Cart.builder()
                .items(List.of())
                .build();
        cartRepository.save(cart);
        return cart.getId();
    }

    public List<CartVo> listAll() {
        List<Cart> cartList = cartRepository.findAll();
        return cartList.stream().map(cart -> cartToVoConverter.convert(cart)).collect(Collectors.toList());
    }

    public CartVo retrieve(final Long cartId) {
        final Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("cart"));
        return cartToVoConverter.convert(cart);
    }

    public void delete(final Long cartId) {
        final Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("cart"));
        cartRepository.delete(cart);
    }
}