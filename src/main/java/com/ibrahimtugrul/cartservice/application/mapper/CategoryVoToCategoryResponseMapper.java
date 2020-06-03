package com.ibrahimtugrul.cartservice.application.mapper;

import com.ibrahimtugrul.cartservice.application.model.response.CategoryResponse;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryVo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CategoryVoToCategoryResponseMapper implements Function<CategoryVo, CategoryResponse> {
    @Override
    public CategoryResponse apply(final CategoryVo categoryVo) {
        return CategoryResponse.builder()
                .id(String.valueOf(categoryVo.getId()))
                .parentId(String.valueOf(categoryVo.getParentId()))
                .title(categoryVo.getTitle())
                .build();
    }
}