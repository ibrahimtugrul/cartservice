package com.ibrahimtugrul.cartservice.application.manager;

import com.ibrahimtugrul.cartservice.application.converter.CategoryCreateRequestToVoConverter;
import com.ibrahimtugrul.cartservice.application.mapper.CategoryVoToCategoryResponseMapper;
import com.ibrahimtugrul.cartservice.application.model.request.CategoryCreateRequest;
import com.ibrahimtugrul.cartservice.application.model.response.CategoryResponse;
import com.ibrahimtugrul.cartservice.application.model.response.IdResponse;
import com.ibrahimtugrul.cartservice.domain.service.CategoryService;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryCreateVo;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryManager {

    private final CategoryService categoryService;
    private final CategoryCreateRequestToVoConverter categoryCreateRequestToVoConverter;
    private final CategoryVoToCategoryResponseMapper categoryVoToCategoryResponseMapper;

    public IdResponse create(final CategoryCreateRequest categoryCreateRequest) {
        final CategoryCreateVo categoryCreateVo = categoryCreateRequestToVoConverter.convert(categoryCreateRequest);
        final long categoryId = categoryService.create(categoryCreateVo);
        return IdResponse.builder().id(String.valueOf(categoryId)).build();
    }

    public List<CategoryResponse> listAll() {
        final List<CategoryVo> categoryVoList = categoryService.listAll();
        return categoryVoList.stream().map(categoryVo -> categoryVoToCategoryResponseMapper.apply(categoryVo)).collect(Collectors.toList());
    }

    public CategoryResponse retrieveCategory(final Long categoryId) {
        final CategoryVo categoryVo = categoryService.retrieve(categoryId);
        return categoryVoToCategoryResponseMapper.apply(categoryVo);
    }

    public void delete(final Long categoryId) {
        categoryService.delete(categoryId);
    }
}