package com.ibrahimtugrul.cartservice.infrastructure.rest;

import com.ibrahimtugrul.cartservice.application.controller.CategoryController;
import com.ibrahimtugrul.cartservice.application.manager.CategoryManager;
import com.ibrahimtugrul.cartservice.application.model.request.CategoryCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CategoryResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class RestCategoryController implements CategoryController {

    private final CategoryManager categoryManager;

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdResponse> create(@Valid @RequestBody final CategoryCreateRequest categoryCreateRequest) {
        final IdResponse response = categoryManager.create(categoryCreateRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponse>> list() {
        final List<CategoryResponse> productList = categoryManager.listAll();
        return ResponseEntity.ok(productList);
    }

    @Override
    @GetMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryResponse> retrieveCategory(@PathVariable("categoryId") final Long categoryId) {
        final CategoryResponse categoryResponse = categoryManager.retrieveCategory(categoryId);
        return ResponseEntity.ok(categoryResponse);
    }

    @Override
    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable("categoryId") final Long categoryId) {
        categoryManager.delete(categoryId);
    }
}