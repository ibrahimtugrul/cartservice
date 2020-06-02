package com.ibrahimtugrul.cartservice.application.controller;

import com.ibrahimtugrul.cartservice.application.model.request.CategoryCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CategoryResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryController {
    ResponseEntity<IdResponse> create(final CategoryCreateRequest categoryCreateRequest);
    ResponseEntity<List<CategoryResponse>> list();
    ResponseEntity<CategoryResponse> retrieveCategory(final Long categoryId);
    void delete(final Long categoryId);
}