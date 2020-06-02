package com.ibrahimtugrul.cartservice.application.converter;

import com.ibrahimtugrul.cartservice.application.model.request.CategoryCreateRequest;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryCreateVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CategoryCreateRequestToVoConverter implements Function<CategoryCreateRequest, CategoryCreateVo>, Converter<CategoryCreateRequest, CategoryCreateVo> {
    @Override
    public CategoryCreateVo apply(final CategoryCreateRequest categoryCreateRequest) {
        return CategoryCreateVo.builder()
                .parentId(categoryCreateRequest.getParentId())
                .title(categoryCreateRequest.getTitle())
                .build();
    }

    @Override
    public CategoryCreateVo convert(final CategoryCreateRequest categoryCreateRequest) {
        return apply(categoryCreateRequest);
    }
}