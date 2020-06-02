package com.ibrahimtugrul.cartservice.application.controller;

import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.application.model.response.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductController {
    ResponseEntity<IdResponse> create(final ProductCreateRequest productCreateRequest);
    ResponseEntity<List<ProductResponse>> list();
    ResponseEntity<ProductResponse> retrieveProduct(final Long productId);
    void delete(final Long productId);
}