package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.controller.ProductController;
import com.ibrahimtugrul.cartservice.application.manager.ProductManager;
import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}