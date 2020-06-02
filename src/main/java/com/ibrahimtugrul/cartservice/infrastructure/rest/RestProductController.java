package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.controller.ProductController;
import com.ibrahimtugrul.cartservice.application.manager.ProductManager;
import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.application.model.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cartservice/v1/product")
@RequiredArgsConstructor
public class RestProductController implements ProductController {

    private final ProductManager productManager;

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdResponse> create(@Valid @RequestBody final ProductCreateRequest productCreateRequest) {
        final IdResponse response = productManager.create(productCreateRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> list() {
        final List<ProductResponse> productList =productManager.listAll();
        return ResponseEntity.ok(productList);
    }

    @Override
    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> retrieveProduct(@PathVariable("productId") final Long productId) {
        final ProductResponse productResponse = productManager.retrieveProduct(productId);
        return ResponseEntity.ok(productResponse);
    }

    @Override
    @DeleteMapping("/{productId}")
    public void delete(@PathVariable("productId") final Long productId) {
        productManager.delete(productId);
    }
}