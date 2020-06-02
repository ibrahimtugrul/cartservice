package com.ibrahimtugrul.cartservice.application.controller;

import com.ibrahimtugrul.cartservice.application.model.request.ProductCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import org.springframework.http.ResponseEntity;

public interface ProductController {
    ResponseEntity<IdResponse> create(final ProductCreateRequest productCreateRequest);
}