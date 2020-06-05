package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.mapper.CartVoToCartResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.response.CartResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.service.CartService;
import com.ibrahimtugrul.cartservice.domain.vo.CartVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartManager {

    private final CartService cartService;
    private final CartVoToCartResponseMapper cartVoToCartResponseMapper;

    public IdResponse create(){
        final Long cartId = cartService.create();
        return IdResponse.builder().id(String.valueOf(cartId)).build();
    }

    public List<CartResponse> listAll() {
        final List<CartVo> cartVoList = cartService.listAll();
        return cartVoList.stream().map(cartVo -> cartVoToCartResponseMapper.apply(cartVo)).collect(Collectors.toList());
    }

    public CartResponse list(final Long cartId) {
        final CartVo cartVo = cartService.retrieve(cartId);
        return cartVoToCartResponseMapper.apply(cartVo);
    }

    public void delete(final Long cartId) {
        cartService.delete(cartId);
    }
}