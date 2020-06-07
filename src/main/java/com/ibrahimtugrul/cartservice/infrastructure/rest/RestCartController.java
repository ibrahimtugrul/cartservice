package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.controller.CartController;
import com.ibrahimtugrul.cartservice.application.manager.CartManager;
import com.ibrahimtugrul.cartservice.application.model.request.CartAddItemRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CartResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class RestCartController implements CartController {

    private final CartManager cartManager;
    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdResponse> create() {
        final IdResponse cartResponse = cartManager.create();
        return ResponseEntity.ok(cartResponse);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CartResponse>> list() {
        final List<CartResponse> cartResponseList = cartManager.listAll();
        return ResponseEntity.ok(cartResponseList);
    }

    @Override
    @GetMapping(value = "/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartResponse> retrieveCart(@PathVariable("cartId") final Long cartId) {
        final CartResponse cartResponse = cartManager.list(cartId);
        return ResponseEntity.ok(cartResponse);
    }

    @Override
    @DeleteMapping("/{cartId}")
    public void delete(@PathVariable("cartId") final Long cartId) {
        cartManager.delete(cartId);
    }

    @Override
    @PutMapping(value = "/{cartId}/item")
    public void addItem(@PathVariable("cartId") final Long cartId, @Valid @RequestBody final CartAddItemRequest cartAddItemRequest) {
        cartManager.addItem(cartId, cartAddItemRequest);
    }
}