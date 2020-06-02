package com.ibrahimtugrul.cartservice.domain.converter;

import com.ibrahimtugrul.cartservice.domain.entity.Category;
import com.ibrahimtugrul.cartservice.domain.vo.CategoryVo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CategoryToVoConverter implements Function<Category, CategoryVo>, Converter<Category, CategoryVo> {
    @Override
    public CategoryVo apply(final Category category) {
        return CategoryVo.builder()
                .id(category.getId())
                .parentId(category.getParentId())
                .title(category.getTitle())
                .build();
    }

    @Override
    public CategoryVo convert(final Category category) {
        return apply(category);
    }
}