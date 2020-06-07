package com.ibrahimtugrul.cartservice.application.controller;

import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CartResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartController {
    ResponseEntity<IdResponse> create();
    ResponseEntity<List<CartResponse>> list();
    ResponseEntity<CartResponse> retrieveCart(final Long cartId);
    void delete(final Long cartId);
    void addItem(final Long cartId, final CartAddItemRequest cartAddItemRequest);
}